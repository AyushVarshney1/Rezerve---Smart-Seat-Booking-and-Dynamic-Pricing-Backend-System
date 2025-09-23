package com.rezerve.rezerveinventoryservice.grpc;

import com.rezerve.rezerveinventoryservice.service.InventoryService;
import inventory.InventoryRequest;
import inventory.InventoryResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcService extends inventory.InventoryServiceGrpc.InventoryServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(InventoryGrpcService.class);
    private final InventoryService inventoryService;

    @Override
    public void bookSeats(InventoryRequest inventoryRequest, StreamObserver<InventoryResponse> responseObserver){
        log.info("BookSeats request recieved: {}", inventoryRequest);

        try{
            boolean seatsBooked = inventoryService.bookSeats(inventoryRequest.getEventId(), inventoryRequest.getTotalSeats());

            InventoryResponse inventoryResponse = InventoryResponse.newBuilder()
                    .setSeatBooked(seatsBooked)
                    .build();

            responseObserver.onNext(inventoryResponse);
            responseObserver.onCompleted();

        }catch (Exception e) {
            log.error("Unexpected error in bookSeats", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Inventory Service internal error")
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

}
