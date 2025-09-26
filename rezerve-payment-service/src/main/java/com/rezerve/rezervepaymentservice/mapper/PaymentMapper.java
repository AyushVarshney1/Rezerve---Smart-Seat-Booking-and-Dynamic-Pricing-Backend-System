package com.rezerve.rezervepaymentservice.mapper;

import auth.AuthResponse;
import com.rezerve.rezervepaymentservice.dto.AuthServiceGrpcResponseDto;
import com.rezerve.rezervepaymentservice.dto.PaymentResponseDto;
import com.rezerve.rezervepaymentservice.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public AuthServiceGrpcResponseDto toAuthServiceGrpcResponseDto(AuthResponse authResponse) {

        AuthServiceGrpcResponseDto authServiceGrpcResponseDto = new AuthServiceGrpcResponseDto();

        authServiceGrpcResponseDto.setUserId(authResponse.getUserId());
        authServiceGrpcResponseDto.setUserEmail(authResponse.getUserEmail());
        authServiceGrpcResponseDto.setUserRole(authResponse.getUserRole());

        return authServiceGrpcResponseDto;

    }

    public PaymentResponseDto toPaymentResponseDto(Payment payment) {

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();

        paymentResponseDto.setPaymentId(payment.getPaymentId());
        paymentResponseDto.setAmount(payment.getAmount());
        paymentResponseDto.setPaymentStatus(payment.getPaymentStatus());
        paymentResponseDto.setUserId(payment.getUserId());
        paymentResponseDto.setId(payment.getId());
        paymentResponseDto.setBookingId(payment.getBookingId());
        paymentResponseDto.setCreatedDate(payment.getCreatedDate());

        return paymentResponseDto;
    }
}
