package com.rezerve.rezervepaymentservice.dto;

import com.rezerve.rezervepaymentservice.model.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PaymentResponseDtoForUser {

    private UUID bookingId;
    private UUID paymentId;
    private Double amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdDate;

}
