package com.bclindner.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * General exception to throw if resources already exist (when the request
 * requires that they do not).
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException() {
        super("Resource already exists");
    }
    public ResourceExistsException(String msg) {
        super(msg);
    }
}