package com.teamknowledgeassistant.common.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response payload returned by the API for failed requests.
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldValidationError> fieldErrors
) {

    public record FieldValidationError(String field, String message) {
    }

    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(LocalDateTime.now(), status, error, message, path, List.of());
    }

    public static ApiError ofFieldErrors(int status, String error, String message, String path,
                                          List<FieldValidationError> fieldErrors) {
        return new ApiError(LocalDateTime.now(), status, error, message, path, fieldErrors);
    }
}
