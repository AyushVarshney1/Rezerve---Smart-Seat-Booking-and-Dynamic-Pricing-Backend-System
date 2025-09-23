package com.rezerve.rezervebookingservice.grpc;

import com.rezerve.rezervebookingservice.dto.EventServiceGrpcResponseDto;
import com.rezerve.rezervebookingservice.exception.EventNotFoundException;
import com.rezerve.rezervebookingservice.exception.GrpcServerException;
import event.EventRequest;
import event.EventResponse;
import com.rezerve.rezervebookingservice.mapper.BookingMapper;
import event.EventServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(EventServiceGrpcClient.class);
    private final EventServiceGrpc.EventServiceBlockingStub blockingStub;
    private final BookingMapper bookingMapper;

    public EventServiceGrpcClient(@Value("${event.service.address:localhost}") String serverAddress, @Value("${event.service.grpc.port:9002}") int serverPort, BookingMapper bookingMapper) {

        log.info("Connecting to Event service GRPC service at {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = EventServiceGrpc.newBlockingStub(channel);

        this.bookingMapper = bookingMapper;
    }

    public EventServiceGrpcResponseDto extractEventInfo(Long eventId){

        EventRequest eventRequest = EventRequest.newBuilder().setEventId(eventId).build();

        try{
            EventResponse eventResponse = blockingStub.extractEventInfo(eventRequest);
            log.info("Received response from Event service via GRPC: {}", eventResponse);

            if(!eventResponse.getExists()){
                throw new EventNotFoundException("Event with id : " + eventId + " not found");
            }

            return bookingMapper.toEventServiceGrpcResponseDto(eventResponse);

        }catch (StatusRuntimeException e) {

            log.error("Event service gRPC error: {} - {}", e.getStatus().getCode(), e.getMessage());
            throw new GrpcServerException("Event service internal server error  " + e.getStatus().getDescription());

        }
    }
}
