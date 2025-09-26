package com.rezerve.rezervepaymentservice.mapper;

import auth.AuthResponse;
import com.rezerve.rezervepaymentservice.dto.AuthServiceGrpcResponseDto;
import com.rezerve.rezervepaymentservice.dto.PaymentRequestDto;
import com.rezerve.rezervepaymentservice.dto.PaymentResponseDto;
import com.rezerve.rezervepaymentservice.dto.PaymentResponseDtoForUser;
import com.rezerve.rezervepaymentservice.model.Payment;
import com.rezerve.rezervepaymentservice.model.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    public PaymentResponseDtoForUser toPaymentResponseDtoForUser(Payment payment) {

        PaymentResponseDtoForUser paymentResponseDtoForUser = new PaymentResponseDtoForUser();

        paymentResponseDtoForUser.setPaymentId(payment.getPaymentId());
        paymentResponseDtoForUser.setAmount(payment.getAmount());
        paymentResponseDtoForUser.setPaymentStatus(payment.getPaymentStatus());
        paymentResponseDtoForUser.setBookingId(payment.getBookingId());
        paymentResponseDtoForUser.setCreatedDate(payment.getCreatedDate());

        return paymentResponseDtoForUser;
    }

    public Payment toPayment(PaymentRequestDto paymentRequestDto, Long userId) {
        Payment payment = new Payment();

        payment.setPaymentId(UUID.randomUUID());
        payment.setAmount(paymentRequestDto.getAmount());
        payment.setBookingId(paymentRequestDto.getBookingId());
        payment.setUserId(userId);

        return payment;

    }
}
