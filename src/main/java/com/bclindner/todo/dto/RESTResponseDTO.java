package com.bclindner.todo.dto;

/**
 * Base REST response data transfer object, used in all requests.
 *
 * This is used to standardize requests, and is generally never used on its own.
 */
public class RESTResponseDTO {
    /**
     * Status of the response.
     */
    public String status;
}
