package com.rezerve.rezervebookingservice.exception;

public class BookingExpiredException extends RuntimeException{
    public BookingExpiredException(String message){
        super(message);
    }
}
