package com.rezerve.rezervebookingservice.service;

import com.rezerve.rezervebookingservice.dto.*;
import com.rezerve.rezervebookingservice.exception.BookingNotFoundException;
import com.rezerve.rezervebookingservice.exception.UnauthorisedException;
import com.rezerve.rezervebookingservice.grpc.AuthServiceGrpcClient;
import com.rezerve.rezervebookingservice.grpc.EventServiceGrpcClient;
import com.rezerve.rezervebookingservice.grpc.InventoryServiceGrpcClient;
import com.rezerve.rezervebookingservice.mapper.BookingMapper;
import com.rezerve.rezervebookingservice.model.Booking;
import com.rezerve.rezervebookingservice.model.enums.BookingStatus;
import com.rezerve.rezervebookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AuthServiceGrpcClient authServiceGrpcClient;
    private final BookingMapper bookingMapper;
    private final EventServiceGrpcClient eventServiceGrpcClient;
    private final InventoryServiceGrpcClient inventoryServiceGrpcClient;

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

    public BookingUserResponseDto createBooking(String token, BookingRequestDto bookingRequestDto){
        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);

        EventServiceGrpcResponseDto eventServiceGrpcResponseDto = eventServiceGrpcClient.extractEventInfo(bookingRequestDto.getEventId());

        inventoryServiceGrpcClient.bookSeats(bookingRequestDto.getEventId(), bookingRequestDto.getTotalTickets());

        Booking booking = bookingMapper.toBooking(bookingRequestDto,authServiceGrpcResponseDto,eventServiceGrpcResponseDto);

        bookingRepository.save(booking);
        return bookingMapper.toBookingUserResponseDto(booking);
    }

    public BookingGrpcResponseDto checkBooking(UUID bookingId){
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);

        if(optionalBooking.isEmpty()){
            return bookingMapper.toBookingGrpcResponseDto("BookingNotFound",null);
        }

        Booking booking = optionalBooking.get();

        if(booking.getBookingStatus().equals(BookingStatus.CANCELLED)){
            return bookingMapper.toBookingGrpcResponseDto("BookingExpired",null);
        }

        if(booking.getBookingStatus().equals(BookingStatus.CONFIRMED)){
            return bookingMapper.toBookingGrpcResponseDto("BookingAlreadyConfirmed",null);
        }

        return bookingMapper.toBookingGrpcResponseDto("BookingFound", booking.getTotalPrice());
    }

    public void completeBooking(UUID bookingId){
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);

        if(optionalBooking.isEmpty()){
            throw new BookingNotFoundException("Booking with id: " + bookingId + " not found");
        }

        Booking booking = optionalBooking.get();

        booking.setBookingStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);
    }
}
