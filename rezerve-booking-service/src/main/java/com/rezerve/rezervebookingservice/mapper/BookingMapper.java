package com.rezerve.rezervebookingservice.mapper;

import auth.AuthResponse;
import com.rezerve.rezervebookingservice.dto.AuthServiceGrpcResponseDto;
import com.rezerve.rezervebookingservice.dto.BookingAdminResponseDto;
import com.rezerve.rezervebookingservice.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingAdminResponseDto toBookingAdminResponseDto(Booking booking){

        BookingAdminResponseDto bookingAdminResponseDto = new BookingAdminResponseDto();

        bookingAdminResponseDto.setId(booking.getId());
        bookingAdminResponseDto.setBookingId(booking.getBookingId());
        bookingAdminResponseDto.setUserId(booking.getUserId());
        bookingAdminResponseDto.setEventId(booking.getEventId());
        bookingAdminResponseDto.setTotalTickets(booking.getTotalTickets());
        bookingAdminResponseDto.setTotalPrice(booking.getTotalPrice());
        bookingAdminResponseDto.setCreatedDate(booking.getCreatedDate());
        bookingAdminResponseDto.setBookingStatus(booking.getBookingStatus());

        return bookingAdminResponseDto;
    }

    public AuthServiceGrpcResponseDto authServiceGrpcResponseDto(AuthResponse authResponse){
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = new AuthServiceGrpcResponseDto();
        authServiceGrpcResponseDto.setUserId(authResponse.getUserId());
        authServiceGrpcResponseDto.setUserEmail(authResponse.getUserEmail());
        authServiceGrpcResponseDto.setUserRole(authResponse.getUserRole());
        return authServiceGrpcResponseDto;
    }
}
