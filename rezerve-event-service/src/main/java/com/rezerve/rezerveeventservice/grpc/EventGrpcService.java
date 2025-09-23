package com.rezerve.rezerveeventservice.grpc;

import com.rezerve.rezerveeventservice.dto.response.EventServiceGrpcResponseDto;
import com.rezerve.rezerveeventservice.service.EventService;
import event.EventRequest;
import event.EventResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class EventGrpcService extends event.EventServiceGrpc.EventServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(EventGrpcService.class.getName());
    private final EventService eventService;

    @Override
    public void extractEventInfo(EventRequest eventRequest, StreamObserver<EventResponse> responseObserver){
        log.info("extractEventInfo request received : {}", eventRequest);

        try{

            EventServiceGrpcResponseDto  eventServiceGrpcResponseDto = eventService.getEventDetailsForBooking(eventRequest.getEventId());

            EventResponse.Builder builder = EventResponse.newBuilder()
                    .setExists(eventServiceGrpcResponseDto.getExists())
                    .setMessage(eventServiceGrpcResponseDto.getMessage());

            if (eventServiceGrpcResponseDto.getEventName() != null) {
                builder.setEventName(eventServiceGrpcResponseDto.getEventName());
            }

            if (eventServiceGrpcResponseDto.getEventCategory() != null) {
                builder.setEventCategory(eventServiceGrpcResponseDto.getEventCategory());
            }

            if (eventServiceGrpcResponseDto.getPrice() != null) {
                builder.setPrice(eventServiceGrpcResponseDto.getPrice());
            }

            EventResponse eventResponse = builder.build();

            responseObserver.onNext(eventResponse);
            responseObserver.onCompleted();

        }catch (Exception e){
            log.error("extractEventInfo exception : {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Event Service internal error")
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
