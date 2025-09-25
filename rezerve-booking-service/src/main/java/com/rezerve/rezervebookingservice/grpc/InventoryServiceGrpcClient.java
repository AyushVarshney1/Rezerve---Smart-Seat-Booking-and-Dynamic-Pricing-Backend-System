package com.rezerve.rezervebookingservice.grpc;

import com.rezerve.rezervebookingservice.exception.EventNotFoundException;
import com.rezerve.rezervebookingservice.exception.GrpcServerException;
import com.rezerve.rezervebookingservice.exception.EventNotBookedException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import inventory.InventoryServiceGrpc;
import inventory.InventoryRequest;
import inventory.InventoryResponse;

@Service
public class InventoryServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceGrpcClient.class.getName());
    private final InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub;

    public InventoryServiceGrpcClient(@Value("${inventory.service.address:localhost}") String serverAddress, @Value("${inventory.service.grpc.port:9004}") int serverPort) {

        log.info("Connecting to Inventory service GRPC service at {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = InventoryServiceGrpc.newBlockingStub(channel);

    }

    public void bookSeats(Long eventId, int totalSeats){

        InventoryRequest inventoryRequest = InventoryRequest.newBuilder()
                .setEventId(eventId)
                .setTotalSeats(totalSeats)
                .build();

        try{
            InventoryResponse inventoryResponse = blockingStub.bookSeats(inventoryRequest);
            log.info("Received response from Inventory service via GRPC: {}", inventoryResponse);

            if(!inventoryResponse.getSeatBooked()){
                if(inventoryResponse.getMessage().equals("EventNotFoundInInventory")){
                    throw new EventNotFoundException("Event with id: " +  eventId + " not found");
                }
                else if(inventoryResponse.getMessage().equals("NotEnoughAvailableSeats")){
                    throw new EventNotBookedException("Not enough available seats");
                }
                else if(inventoryResponse.getMessage().equals("EventFullyBooked")){
                    throw new EventNotBookedException("Event has been fully booked");
                }
            }



        }catch (StatusRuntimeException e) {

            log.error("Inventory service gRPC error: {} - {}", e.getStatus().getCode(), e.getMessage());
            throw new GrpcServerException("Inventory service internal server error  " + e.getStatus().getDescription());

        }
    }
}
