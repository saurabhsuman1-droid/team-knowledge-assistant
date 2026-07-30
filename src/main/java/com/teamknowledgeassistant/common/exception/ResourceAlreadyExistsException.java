package com.teamknowledgeassistant.common.exception;

/**
 * Thrown when attempting to create or update a resource that violates uniqueness constraints.
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
