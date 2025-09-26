package com.rezerve.rezervebookingservice.mapper;

import auth.AuthResponse;
import com.rezerve.rezervebookingservice.dto.*;
import com.rezerve.rezervebookingservice.model.Booking;
import com.rezerve.rezervebookingservice.model.enums.BookingStatus;
import com.rezerve.rezervebookingservice.model.enums.EventCategory;
import event.EventResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingMapper {

    public BookingAdminResponseDto toBookingAdminResponseDto(Booking booking){

        BookingAdminResponseDto bookingAdminResponseDto = new BookingAdminResponseDto();

        bookingAdminResponseDto.setId(booking.getId());
        bookingAdminResponseDto.setBookingId(booking.getBookingId());
        bookingAdminResponseDto.setUserId(booking.getUserId());
        bookingAdminResponseDto.setEventId(booking.getEventId());
        bookingAdminResponseDto.setEventName(booking.getEventName());
        bookingAdminResponseDto.setEventCategory(booking.getEventCategory());
        bookingAdminResponseDto.setTotalTickets(booking.getTotalTickets());
        bookingAdminResponseDto.setTotalPrice(booking.getTotalPrice());
        bookingAdminResponseDto.setCreatedDate(booking.getCreatedDate());
        bookingAdminResponseDto.setBookingStatus(booking.getBookingStatus());

        return bookingAdminResponseDto;
    }

    public AuthServiceGrpcResponseDto toAuthServiceGrpcResponseDto(AuthResponse authResponse){

        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = new AuthServiceGrpcResponseDto();

        authServiceGrpcResponseDto.setUserId(authResponse.getUserId());
        authServiceGrpcResponseDto.setUserEmail(authResponse.getUserEmail());
        authServiceGrpcResponseDto.setUserRole(authResponse.getUserRole());

        return authServiceGrpcResponseDto;
    }

    public EventServiceGrpcResponseDto toEventServiceGrpcResponseDto(EventResponse eventResponse){

        EventServiceGrpcResponseDto eventServiceGrpcResponseDto = new EventServiceGrpcResponseDto();

        eventServiceGrpcResponseDto.setPrice(eventResponse.getPrice());
        eventServiceGrpcResponseDto.setEventName(eventResponse.getEventName());
        eventServiceGrpcResponseDto.setEventCategory(EventCategory.valueOf(eventResponse.getEventCategory()));

        return eventServiceGrpcResponseDto;
    }

    public Booking toBooking(BookingRequestDto bookingRequestDto, AuthServiceGrpcResponseDto authServiceGrpcResponseDto, EventServiceGrpcResponseDto eventServiceGrpcResponseDto){

        Booking booking = new Booking();

        booking.setUserId(authServiceGrpcResponseDto.getUserId());
        booking.setEventId(bookingRequestDto.getEventId());
        booking.setEventName(eventServiceGrpcResponseDto.getEventName());
        booking.setEventCategory(eventServiceGrpcResponseDto.getEventCategory());
        booking.setTotalTickets(bookingRequestDto.getTotalTickets());
        booking.setTotalPrice(bookingRequestDto.getTotalTickets() * eventServiceGrpcResponseDto.getPrice());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setBookingId(UUID.randomUUID());

        return booking;
    }

    public BookingUserResponseDto toBookingUserResponseDto(Booking booking){

        BookingUserResponseDto bookingUserResponseDto = new BookingUserResponseDto();

        bookingUserResponseDto.setBookingId(booking.getBookingId());
        bookingUserResponseDto.setEventName(booking.getEventName());
        bookingUserResponseDto.setEventCategory(booking.getEventCategory());
        bookingUserResponseDto.setTotalTickets(booking.getTotalTickets());
        bookingUserResponseDto.setTotalPrice(booking.getTotalPrice());
        bookingUserResponseDto.setCreatedDate(booking.getCreatedDate());

        return bookingUserResponseDto;
    }

    public BookingGrpcResponseDto toBookingGrpcResponseDto(String message, Double totalPrice){

        BookingGrpcResponseDto bookingGrpcResponseDto = new BookingGrpcResponseDto();

        bookingGrpcResponseDto.setExists(message.equals("BookingFound"));
        bookingGrpcResponseDto.setMessage(message);

        if(totalPrice != null){
            bookingGrpcResponseDto.setTotalPrice(totalPrice);
        }

        return bookingGrpcResponseDto;
    }
}
