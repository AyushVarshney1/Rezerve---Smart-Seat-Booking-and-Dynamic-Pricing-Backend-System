package com.rezerve.rezerveinventoryservice.service;

import com.rezerve.rezerveinventoryservice.dto.EventSeatInfo;
import com.rezerve.rezerveinventoryservice.dto.InventoryEventConsumerDto;
import com.rezerve.rezerveinventoryservice.dto.InventoryGrpcResponseDto;
import com.rezerve.rezerveinventoryservice.exception.EventNotFoundException;
import com.rezerve.rezerveinventoryservice.mapper.InventoryMapper;
import com.rezerve.rezerveinventoryservice.model.Inventory;
import com.rezerve.rezerveinventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final RedisTemplate<String, EventSeatInfo> redisTemplate;
    private final RedissonClient redissonClient;

    private static final String INVENTORY_CACHE_PREFIX = "inventory:";

    @Transactional
    public InventoryGrpcResponseDto bookSeats(Long eventId, int totalSeatsToBook) {
        int maxRetries = 5;
        int baseDelayMs = 50;

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            String lockKey = "lock:inventory:" + eventId;
            String cacheKey = INVENTORY_CACHE_PREFIX + eventId;

            RLock lock = redissonClient.getLock(lockKey);

            boolean lockAcquired = false;
            try {
                lockAcquired = lock.tryLock(100, 5000, TimeUnit.MILLISECONDS);

                if (!lockAcquired) {
                    if (attempt < maxRetries - 1) {
                        int delay = baseDelayMs * (int) Math.pow(2, attempt);
                        Thread.sleep(delay);
                        continue;
                    } else {
                        return inventoryMapper.toInventoryGrpcResponseDto(
                                false,
                                "SystemBusy"
                        );
                    }
                }

                EventSeatInfo eventSeatInfo = redisTemplate.opsForValue().get(cacheKey);

                if (eventSeatInfo == null) {
                    Optional<Inventory> optionalInventory = inventoryRepository.findByEventId(eventId);
                    if (optionalInventory.isEmpty()) {
                        return inventoryMapper.toInventoryGrpcResponseDto(false, "EventNotFoundInInventory");
                    }

                    Inventory inventory = optionalInventory.get();
                    eventSeatInfo = new EventSeatInfo(inventory.getAvailableSeats(), inventory.getTotalSeats());
                }

                if (eventSeatInfo.getAvailableSeats() == 0) {
                    return inventoryMapper.toInventoryGrpcResponseDto(false, "EventFullyBooked");
                }

                if (eventSeatInfo.getAvailableSeats() < totalSeatsToBook) {
                    return inventoryMapper.toInventoryGrpcResponseDto(false, "NotEnoughAvailableSeats");
                }

                // BELOW DB CALL IS AN ATOMIC TRANSACTION WHICH WILL PREVENT RACE CONDITION
                int updatedRows = inventoryRepository.updateAvailableSeats(
                        eventId,
                        totalSeatsToBook
                );

                if (updatedRows == 0) {
                    redisTemplate.delete(cacheKey);
                    if (attempt < maxRetries - 1) {
                        continue;
                    }
                    return inventoryMapper.toInventoryGrpcResponseDto(false, "BookingFailed");
                }

                eventSeatInfo.setAvailableSeats(eventSeatInfo.getAvailableSeats() - totalSeatsToBook);
                redisTemplate.opsForValue().set(cacheKey, eventSeatInfo);

                return inventoryMapper.toInventoryGrpcResponseDto(true, "SeatBooked");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return inventoryMapper.toInventoryGrpcResponseDto(false, "BookingInterrupted");
            } finally {
                if (lockAcquired) {
                    lock.unlock();
                }
            }
        }

        return inventoryMapper.toInventoryGrpcResponseDto(false, "BookingFailed");
    }

    public void createInventory(InventoryEventConsumerDto inventoryEventConsumerDto) {
        Inventory inventory = new Inventory();

        inventory.setEventId(inventoryEventConsumerDto.getEventId());
        inventory.setTotalSeats(inventoryEventConsumerDto.getTotalSeats());
        inventory.setAvailableSeats(inventoryEventConsumerDto.getTotalSeats());
        inventory.setEventCategory(inventoryEventConsumerDto.getEventCategory());

        inventoryRepository.save(inventory);

        String cacheKey = INVENTORY_CACHE_PREFIX + inventoryEventConsumerDto.getEventId();
        EventSeatInfo eventSeatInfo = new EventSeatInfo(
                inventoryEventConsumerDto.getTotalSeats(),
                inventoryEventConsumerDto.getTotalSeats()
        );
        redisTemplate.opsForValue().set(cacheKey, eventSeatInfo);
    }

    @Transactional
    public void updateEventSeats(InventoryEventConsumerDto inventoryEventConsumerDto) {
        Inventory inventory = inventoryRepository.findByEventId(inventoryEventConsumerDto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + inventoryEventConsumerDto.getEventId() + " not found"));

        int bookedSeats = inventory.getTotalSeats() - inventory.getAvailableSeats();
        int newTotal = inventoryEventConsumerDto.getTotalSeats();

        if (newTotal < bookedSeats) {
            throw new IllegalStateException("Cannot reduce total seats below already booked seats");
        }

        inventory.setAvailableSeats(newTotal - bookedSeats);
        inventory.setTotalSeats(newTotal);

        inventoryRepository.save(inventory);

        String cacheKey = INVENTORY_CACHE_PREFIX + inventoryEventConsumerDto.getEventId();
        EventSeatInfo eventSeatInfo = new EventSeatInfo(
                inventory.getAvailableSeats(),
                inventory.getTotalSeats()
        );
        redisTemplate.opsForValue().set(cacheKey, eventSeatInfo);
    }

    @Transactional
    public void deleteInventory(InventoryEventConsumerDto inventoryEventConsumerDto) {
        if (inventoryRepository.findByEventId(inventoryEventConsumerDto.getEventId()).isEmpty()) {
            throw new EventNotFoundException("Event with id: " + inventoryEventConsumerDto.getEventId() + " not found");
        }

        inventoryRepository.deleteByEventId(inventoryEventConsumerDto.getEventId());

        String cacheKey = INVENTORY_CACHE_PREFIX + inventoryEventConsumerDto.getEventId();
        redisTemplate.delete(cacheKey);
    }

    @Transactional
    public void releaseSeat(Long eventId, Integer seatsToRelease) {
        int maxRetries = 5;
        int baseDelayMs = 50;

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            String lockKey = "lock:inventory:" + eventId;
            String cacheKey = INVENTORY_CACHE_PREFIX + eventId;

            RLock lock = redissonClient.getLock(lockKey);
            boolean lockAcquired = false;

            try {
                lockAcquired = lock.tryLock(100, 5000, TimeUnit.MILLISECONDS);

                if (!lockAcquired) {
                    if (attempt < maxRetries - 1) {
                        int delay = baseDelayMs * (int) Math.pow(2, attempt);
                        Thread.sleep(delay);
                        continue;
                    } else {
                        throw new RuntimeException("Failed to acquire lock for releasing seats after " + maxRetries + " attempts");
                    }
                }

                Optional<Inventory> optionalInventory = inventoryRepository.findByEventId(eventId);

                if (optionalInventory.isEmpty()) {
                    throw new EventNotFoundException("Event with id: " + eventId + " not found");
                }

                Inventory inventory = optionalInventory.get();
                int currentAvailableSeats = inventory.getAvailableSeats();
                int newAvailableSeats = currentAvailableSeats + seatsToRelease;

                if (newAvailableSeats > inventory.getTotalSeats()) {
                    newAvailableSeats = inventory.getTotalSeats();
                }

                inventory.setAvailableSeats(newAvailableSeats);
                inventoryRepository.save(inventory);

                EventSeatInfo eventSeatInfo = new EventSeatInfo(
                        inventory.getAvailableSeats(),
                        inventory.getTotalSeats()
                );
                redisTemplate.opsForValue().set(cacheKey, eventSeatInfo);

                return;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while releasing seats", e);
            } catch (Exception e) {
                throw new RuntimeException("Failed to release seats: " + e.getMessage());
            } finally {
                if (lockAcquired && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }
}
