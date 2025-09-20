package ru.practicum.exception;

public class AccessDeniedForProfileException extends RuntimeException {
    public AccessDeniedForProfileException(String message) {
        super(message);
    }
}
