package com.rezerve.rezerveinventoryservice.service;

import com.rezerve.rezerveinventoryservice.dto.InventoryEventConsumerDto;
import com.rezerve.rezerveinventoryservice.dto.InventoryGrpcResponseDto;
import com.rezerve.rezerveinventoryservice.exception.EventNotFoundException;
import com.rezerve.rezerveinventoryservice.mapper.InventoryMapper;
import com.rezerve.rezerveinventoryservice.model.Inventory;
import com.rezerve.rezerveinventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryGrpcResponseDto bookSeats(Long eventId, int totalSeatsToBook){
        Optional<Inventory> optionalInventory = inventoryRepository.findByEventId(eventId);

        if(optionalInventory.isEmpty()){
            return inventoryMapper.toInventoryGrpcResponseDto(false, "EventNotFoundInInventory");
        }

        Inventory inventory = optionalInventory.get();

        if(inventory.getAvailableSeats() == 0){
            return inventoryMapper.toInventoryGrpcResponseDto(false, "EventFullyBooked");
        }

        if(inventory.getAvailableSeats() < totalSeatsToBook){
            return inventoryMapper.toInventoryGrpcResponseDto(false, "NotEnoughAvailableSeats");
        }

        inventory.setAvailableSeats(inventory.getAvailableSeats() - totalSeatsToBook);
        inventoryRepository.save(inventory);

        return inventoryMapper.toInventoryGrpcResponseDto(true, "SeatBooked");
    }

    public void createInventory(InventoryEventConsumerDto inventoryEventConsumerDto){
        Inventory inventory = new Inventory();

        inventory.setEventId(inventoryEventConsumerDto.getEventId());
        inventory.setTotalSeats(inventoryEventConsumerDto.getTotalSeats());
        inventory.setAvailableSeats(inventoryEventConsumerDto.getTotalSeats());

        inventoryRepository.save(inventory);
    }

    @Transactional
    public void updateEventSeats(InventoryEventConsumerDto inventoryEventConsumerDto){
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
    }

    @Transactional
    public void deleteInventory(InventoryEventConsumerDto inventoryEventConsumerDto){
        if(inventoryRepository.findByEventId(inventoryEventConsumerDto.getEventId()).isEmpty()){
            throw new EventNotFoundException("Event with id: " + inventoryEventConsumerDto.getEventId() + " not found");
        }

        inventoryRepository.deleteByEventId(inventoryEventConsumerDto.getEventId());
    }
}
