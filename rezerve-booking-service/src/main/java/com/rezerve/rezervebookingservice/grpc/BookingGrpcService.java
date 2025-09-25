package com.rezerve.rezervebookingservice.grpc;

import booking.BookingRequest;
import booking.BookingResponse;
import com.rezerve.rezervebookingservice.dto.BookingGrpcResponseDto;
import com.rezerve.rezervebookingservice.service.BookingService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class BookingGrpcService extends booking.BookingServiceGrpc.BookingServiceImplBase {

    private static final Logger log =  LoggerFactory.getLogger(BookingGrpcService.class);
    private final BookingService bookingService;

    @Override
    public void checkBooking(BookingRequest bookingRequest, StreamObserver<BookingResponse> responseObserver){
        log.info("check booking request received  {}", bookingRequest);

        try{
            BookingGrpcResponseDto bookingGrpcResponseDto = bookingService.checkBooking(UUID.fromString(bookingRequest.getBookingId()));

            BookingResponse.Builder bookingResponseBuilder = BookingResponse.newBuilder()
                    .setExists(bookingGrpcResponseDto.getExists())
                    .setMessage(bookingGrpcResponseDto.getMessage());

            if(bookingGrpcResponseDto.getTotalPrice() != null){
                bookingResponseBuilder.setTotalPrice(bookingGrpcResponseDto.getTotalPrice());
            }

            BookingResponse bookingResponse = bookingResponseBuilder.build();

            responseObserver.onNext(bookingResponse);
            responseObserver.onCompleted();

        }catch (Exception e) {
            log.error("Unexpected error in checkBooking", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Booking Service internal error")
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
