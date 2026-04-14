package com.rrosenthal.corrix.exception;

import java.util.List;

public class StageValidationException extends RuntimeException {

    private final List<String> errors;

    public StageValidationException(String message, List<String> errors) {
        super(message);
        this.errors = List.copyOf(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
