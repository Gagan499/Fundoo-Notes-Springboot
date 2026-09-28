package com.fundoo.notes.execption;

public class AlreadyNoteIsTrashed extends RuntimeException {
    public AlreadyNoteIsTrashed(String message) {
        super(message);
    }
}
