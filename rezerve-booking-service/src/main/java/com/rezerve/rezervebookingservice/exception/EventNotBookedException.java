package com.rezerve.rezervebookingservice.exception;

public class EventNotBookedException extends RuntimeException {
    public EventNotBookedException(String message) {
        super(message);
    }
}
