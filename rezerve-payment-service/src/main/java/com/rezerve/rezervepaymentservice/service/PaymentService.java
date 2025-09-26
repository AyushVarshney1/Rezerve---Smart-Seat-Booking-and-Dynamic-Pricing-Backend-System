package com.rezerve.rezervepaymentservice.service;

import com.rezerve.rezervepaymentservice.dto.AuthServiceGrpcResponseDto;
import com.rezerve.rezervepaymentservice.dto.PaymentResponseDto;
import com.rezerve.rezervepaymentservice.exception.PaymentNotFoundException;
import com.rezerve.rezervepaymentservice.exception.UnauthorisedException;
import com.rezerve.rezervepaymentservice.grpc.AuthServiceGrpcClient;
import com.rezerve.rezervepaymentservice.grpc.BookingServiceGrpcClient;
import com.rezerve.rezervepaymentservice.mapper.PaymentMapper;
import com.rezerve.rezervepaymentservice.model.Payment;
import com.rezerve.rezervepaymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingServiceGrpcClient bookingServiceGrpcClient;
    private final AuthServiceGrpcClient authServiceGrpcClient;
    private final PaymentMapper paymentMapper;

    public List<PaymentResponseDto> getAllPayments(String token){
        AuthServiceGrpcResponseDto  authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);

        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only Admin Can Access Payments");
        }

        List<Payment> payments = paymentRepository.findAll();

        return payments.stream().map(paymentMapper::toPaymentResponseDto).collect(Collectors.toList());
    }

    public PaymentResponseDto getPaymentById(String token, UUID paymentId){
        AuthServiceGrpcResponseDto  authServiceGrpcResponseDto = authServiceGrpcClient.extractUserInfo(token);

        if(!authServiceGrpcResponseDto.getUserRole().equals("ADMIN")){
            throw new UnauthorisedException("Only Admin Can Access Payments");
        }

        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(() -> new PaymentNotFoundException("Payment with id: " + paymentId + " not found"));

        return paymentMapper.toPaymentResponseDto(payment);
    }
}
