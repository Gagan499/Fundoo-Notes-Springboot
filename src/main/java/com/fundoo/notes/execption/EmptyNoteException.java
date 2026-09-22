package com.fundoo.notes.execption;

public class EmptyNoteException extends RuntimeException{
    private String message;
    public EmptyNoteException(String message){
        super(message);
    }
}
