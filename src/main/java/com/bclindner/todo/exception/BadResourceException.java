package com.bclindner.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadResourceException extends RuntimeException {
    public BadResourceException() {
        super("Resource is malformatted");
    }
    public BadResourceException(String msg) {
        super(msg);
    }
}
