package com.example.UniversityWorkshopRegistrationSystem.exception;

// Thrown when a user tries to cancel someone else's registration.
// Maps to HTTP 403 Forbidden.
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
