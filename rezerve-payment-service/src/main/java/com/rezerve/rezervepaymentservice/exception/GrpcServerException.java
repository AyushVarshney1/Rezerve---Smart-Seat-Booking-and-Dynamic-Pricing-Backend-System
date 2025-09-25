package com.rezerve.rezervepaymentservice.exception;

public class GrpcServerException extends RuntimeException {
    public GrpcServerException(String message) {
        super(message);
    }
}
