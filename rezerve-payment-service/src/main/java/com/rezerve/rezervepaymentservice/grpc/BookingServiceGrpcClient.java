package com.rezerve.rezervepaymentservice.grpc;

import booking.BookingRequest;
import booking.BookingResponse;
import booking.BookingServiceGrpc;
import com.rezerve.rezervepaymentservice.exception.BookingNotAllowedException;
import com.rezerve.rezervepaymentservice.exception.BookingNotFoundException;
import com.rezerve.rezervepaymentservice.exception.GrpcServerException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceGrpcClient.class.getName());
    private final BookingServiceGrpc.BookingServiceBlockingStub blockingStub;

    public BookingServiceGrpcClient(@Value("${booking.service.address:localhost}") String serverAddress, @Value("${booking.service.grpc.port:9003}") int serverPort){

        log.info("Booking Service Grpc Client Started at: {}, {}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();

        blockingStub =  BookingServiceGrpc.newBlockingStub(channel);
    }

    public Double checkBooking(UUID bookingId){

        BookingRequest bookingRequest = BookingRequest.newBuilder()
                .setBookingId(String.valueOf(bookingId))
                .build();

        try{
            BookingResponse bookingResponse = blockingStub.checkBooking(bookingRequest);
            log.info("Booking Response from BookingServiceGrpc is {}", bookingResponse);

            if(!bookingResponse.getExists()){
                if(bookingResponse.getMessage().equals("BookingNotFound")){
                    throw new BookingNotFoundException("Booking with id " + bookingId + " not found");
                }
                if(bookingResponse.getMessage().equals("BookingExpired")){
                    throw new BookingNotAllowedException("Booking with id " + bookingId + " has expired");
                }
                if(bookingResponse.getMessage().equals("BookingAlreadyConfirmed")){
                    throw new BookingNotAllowedException("Booking with id " + bookingId + " has already been confirmed");
                }
            }

            return bookingResponse.getTotalPrice();

        }catch (StatusRuntimeException e) {

            log.error("Booking service gRPC error: {} - {}", e.getStatus().getCode(), e.getMessage());
            throw new GrpcServerException("Booking service internal server error  " + e.getStatus().getDescription());

        }
    }
}
