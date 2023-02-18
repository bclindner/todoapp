package com.bclindner.todo.exception;

/**
 * General exception to throw if resources already exist (when the request
 * requires that they do not).
 */
public class ResourceExistsException extends RuntimeException {
}