package com.fundoo.notes.execption;

public class NoteNotFoundByIdException extends RuntimeException {
    public NoteNotFoundByIdException(String message) {
        super(message);
    }
}
