package com.rezerve.rezervebookingservice.exception;

public class GrpcServerException extends RuntimeException {
    public GrpcServerException(String message) {
        super(message);
    }
}
