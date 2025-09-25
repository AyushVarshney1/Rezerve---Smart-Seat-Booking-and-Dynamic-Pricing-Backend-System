package com.rezerve.rezervepaymentservice.service;

import com.rezerve.rezervepaymentservice.grpc.BookingServiceGrpcClient;
import com.rezerve.rezervepaymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingServiceGrpcClient bookingServiceGrpcClient;


}
