package ru.practicum.exception;

public class ProfileDeactivatedException extends RuntimeException {
    public ProfileDeactivatedException() {
        super("Profile deactivated");
    }

    public ProfileDeactivatedException(String message) {
        super(message);
    }
}
