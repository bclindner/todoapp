package com.bclindner.todo.dto;

/**
 * Response containing data.
 * This should only be used with 2xx responses.
 */
public class DataResponseDTO<T> extends RESTResponseDTO {
    public T data;

    /**
     * Create a new DataResponseDTO with the data object.
     * @param data
     */
    public DataResponseDTO(T data) {
        this.status = "success";
        this.data = data;
    }
}
