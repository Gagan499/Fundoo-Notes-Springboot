package com.fundoo.notes.execption;

public class UserAlreadyExistsException  extends RuntimeException{
    private String message;

    public UserAlreadyExistsException(String s){
        super(s);
    }

}
