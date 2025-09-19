package com.rezerve.rezervebookingservice.service;

import com.rezerve.rezervebookingservice.dto.AuthServiceGrpcResponseDto;
import com.rezerve.rezervebookingservice.dto.BookingAdminResponseDto;
import com.rezerve.rezervebookingservice.exception.BookingNotFoundException;
import com.rezerve.rezervebookingservice.exception.UnauthorisedException;
import com.rezerve.rezervebookingservice.grpc.AuthServiceGrpcClient;
import com.rezerve.rezervebookingservice.mapper.BookingMapper;
import com.rezerve.rezervebookingservice.model.Booking;
import com.rezerve.rezervebookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AuthServiceGrpcClient authServiceGrpcClient;
    private final BookingMapper bookingMapper;

    public List<BookingAdminResponseDto> getAllBookings(String token){
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);

        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only Admin Can Access Bookings");
        }

        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream().map(bookingMapper::toBookingAdminResponseDto).collect(Collectors.toList());
    }

    public BookingAdminResponseDto getBookingById(String token, Long bookingId){
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);

        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only Admin Can Access Bookings");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: " + bookingId + " not found"));

        return bookingMapper.toBookingAdminResponseDto(booking);
    }
}
