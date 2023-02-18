package com.bclindner.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.bclindner.todo.dto.ErrorResponseDTO;
import com.bclindner.todo.exception.ResourceExistsException;
import com.bclindner.todo.exception.ResourceNotFoundException;

/**
 * Global RestController exception handler.
 *
 * This allows for uniform returns which return only pertinent information for
 * downstream clients for security and utility.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handle requests which have no valid handler.
     * Just returns a 404 and an empty body.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noHandlerFound() {
    }
    
    /**
     * Handle requests to handle creating resources that already exist.
     * Returns 400 and an error message.
     * @param exception
     * @return
     */
    @ExceptionHandler(ResourceExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO resourceExists(ResourceExistsException exception) {
        return new ErrorResponseDTO(exception);
    }
    
    /**
     * Handle requests which are attempting to get resources that are not
     * found.
     * Returns 204 and an empty body.
     * @param exception
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resourceNotFound(ResourceExistsException exception) {
    }
    
    /**
     * Handle all other Exceptions with a generic internal server error
     * response for data security.
     * @param exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO genericError() {
        return new ErrorResponseDTO(new Exception("Internal server error"));
    }
}
