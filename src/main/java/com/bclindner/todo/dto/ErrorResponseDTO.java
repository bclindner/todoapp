package com.bclindner.todo.dto;

import java.time.Instant;
import java.util.Date;

/**
 * Uniform error response in the REST API.
 * This should only be used with 4xx/5xx responses.
 */
public class ErrorResponseDTO extends RESTResponseDTO {
    /**
     * Error message.
     */
    public String error;
    /**
     * Timetamp for the error.
     */
    public Date timestamp;

    /**
     * Generate an ErrorResponse.
     * @param error Error message.
     */
    public ErrorResponseDTO(Exception exception) {

        this.status = "error";
        this.error = exception.getMessage();
        // fill the timestamp from the current time
        this.timestamp = Date.from(Instant.now());
    }
}
