package com.rezerve.rezervebookingservice.controller;

import com.rezerve.rezervebookingservice.dto.BookingAdminResponseDto;
import com.rezerve.rezervebookingservice.dto.BookingRequestDto;
import com.rezerve.rezervebookingservice.dto.BookingUserResponseDto;
import com.rezerve.rezervebookingservice.exception.InvalidHeaderException;
import com.rezerve.rezervebookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    // GET ALL BOOKINGS (CAN ONLY BE ACCESSED BY ADMIN)
    @GetMapping
    public ResponseEntity<List<BookingAdminResponseDto>> getAllBookings(@RequestHeader("Authorization") String header){
        if(header == null || !header.startsWith("Bearer ")){
            throw new InvalidHeaderException("Invalid Authorization header");
        }

        String token = header.substring(7);

        List<BookingAdminResponseDto> bookingAdminResponseDtos = bookingService.getAllBookings(token);

        return ResponseEntity.ok(bookingAdminResponseDtos);
    }

    // GET BOOKING BY ID (CAN ONLY BE ACCESSED BY ADMIN)
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingAdminResponseDto> getBooking(@RequestHeader("Authorization") String header, @PathVariable("bookingId") Long bookingId){
        if(header == null || !header.startsWith("Bearer ")){
            throw new InvalidHeaderException("Invalid Authorization header");
        }

        String token = header.substring(7);

        BookingAdminResponseDto bookingAdminResponseDto = bookingService.getBookingById(token, bookingId);

        return ResponseEntity.ok(bookingAdminResponseDto);
    }

    // CREATE BOOKING
    @PostMapping
    public ResponseEntity<BookingUserResponseDto> createBooking(@RequestHeader("Authorization") String header, @Valid @RequestBody BookingRequestDto  bookingRequestDto){
        if(header == null || !header.startsWith("Bearer ")){
            throw new InvalidHeaderException("Invalid Authorization header");
        }

        String token = header.substring(7);

        BookingUserResponseDto bookingUserResponseDto = bookingService.createBooking(token,bookingRequestDto);

        return ResponseEntity.ok(bookingUserResponseDto);
    }

}
