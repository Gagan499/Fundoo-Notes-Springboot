package com.fundoo.notes.execption;

public class TitleNotEmptyOrNull extends RuntimeException {
    public TitleNotEmptyOrNull(String message) {
        super(message);
    }
}
