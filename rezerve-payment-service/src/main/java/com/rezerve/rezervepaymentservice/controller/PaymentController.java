package com.rezerve.rezervepaymentservice.controller;

import com.rezerve.rezervepaymentservice.dto.PaymentRequestDto;
import com.rezerve.rezervepaymentservice.dto.PaymentResponseDto;
import com.rezerve.rezervepaymentservice.dto.PaymentResponseDtoForUser;
import com.rezerve.rezervepaymentservice.exception.InvalidHeaderException;
import com.rezerve.rezervepaymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    // GET ALL PAYMENTS (ONLY ADMIN CAN ACCESS)
    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments(@RequestHeader("Authorization") String header){
        if(header == null || !header.startsWith("Bearer ")){
            throw new InvalidHeaderException("Authorization header is invalid");
        }

        String token =  header.substring(7);

        List<PaymentResponseDto> paymentResponseDtos = paymentService.getAllPayments(token);

        return ResponseEntity.ok(paymentResponseDtos);
    }

    // GET PAYMENT BY ID (ONLY ADMIN CAN ACCESS)
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@RequestHeader("Authorization") String header, @PathVariable("paymentId") UUID paymentId){
        if(header == null || !header.startsWith("Bearer ")){
            throw new InvalidHeaderException("Authorization header is invalid");
        }

        String token =  header.substring(7);

        PaymentResponseDto paymentResponseDto = paymentService.getPaymentById(token,paymentId);

        return ResponseEntity.ok(paymentResponseDto);
    }

    // CREATE PAYMENT (COMPLETE BOOKING PROCESS)
    @PostMapping
    public ResponseEntity<PaymentResponseDtoForUser> createPayment(@RequestHeader("Authorization") String header, @Valid @RequestBody PaymentRequestDto paymentRequestDto){
        if(header == null || !header.startsWith("Bearer ")){
            throw new InvalidHeaderException("Authorization header is invalid");
        }

        String token =  header.substring(7);

        PaymentResponseDtoForUser paymentResponseDtoForUser = paymentService.createPayment(token,paymentRequestDto);

        return ResponseEntity.ok(paymentResponseDtoForUser);
    }
}
