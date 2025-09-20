package ru.practicum.exception;

public class ProfileDeactivatedException extends RuntimeException {
    public ProfileDeactivatedException(String message) {
        super(message);
    }
}
