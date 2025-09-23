package com.rezerve.rezerveinventoryservice.service;

import com.rezerve.rezerveinventoryservice.model.Inventory;
import com.rezerve.rezerveinventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
