package com.rezerve.rezerveinventoryservice.service;

import com.rezerve.rezerveinventoryservice.dto.InventoryEventConsumerDto;
import com.rezerve.rezerveinventoryservice.exception.EventNotFoundException;
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

    public boolean bookSeats(Long eventId, int totalSeatsToBook){
        Optional<Inventory> optionalInventory = inventoryRepository.findByEventId(eventId);

        if(optionalInventory.isEmpty()){
            return false;
        }

        Inventory inventory = optionalInventory.get();

        if(inventory.getAvailableSeats() < totalSeatsToBook){
            return false;
        }

        inventory.setAvailableSeats(inventory.getAvailableSeats() - totalSeatsToBook);
        inventoryRepository.save(inventory);

        return true;
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

    public void deleteInventory(InventoryEventConsumerDto inventoryEventConsumerDto){
        if(inventoryRepository.findByEventId(inventoryEventConsumerDto.getEventId()).isEmpty()){
            throw new EventNotFoundException("Event with id: " + inventoryEventConsumerDto.getEventId() + " not found");
        }

        inventoryRepository.deleteByEventId(inventoryEventConsumerDto.getEventId());
    }
}
