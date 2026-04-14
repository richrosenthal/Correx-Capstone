package com.rrosenthal.corrix.exception;

import java.util.List;

public class ActionItemValidationException extends RuntimeException {

    private final List<String> errors;

    public ActionItemValidationException(String message, List<String> errors) {
        super(message);
        this.errors = List.copyOf(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
