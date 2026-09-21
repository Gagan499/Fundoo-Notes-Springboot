package com.fundoo.notes.execption;


public class InvalidCredentialsException extends RuntimeException {
    private String message;
    public InvalidCredentialsException(String message){
        super(message);
    }
}
