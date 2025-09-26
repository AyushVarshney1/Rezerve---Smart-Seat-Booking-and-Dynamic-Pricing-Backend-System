package com.rezerve.rezervepaymentservice.dto;

import com.rezerve.rezervepaymentservice.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PaymentResponseDto {

    private Long id;
    private UUID paymentId;
    private UUID bookingId;
    private Long userId;
    private Double amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdDate;

}
