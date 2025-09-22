package com.rezerve.rezervebookingservice.exception;

public class EventSoldOutException extends RuntimeException {
    public EventSoldOutException(String message) {
        super(message);
    }
}
