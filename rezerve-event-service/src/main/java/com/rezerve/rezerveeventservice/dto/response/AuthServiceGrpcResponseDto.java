package com.rezerve.rezerveeventservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthServiceGrpcResponseDto {

    private Long userId;
    private String userEmail;
    private String userRole;
}
