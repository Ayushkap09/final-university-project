package com.example.UniversityWorkshopRegistrationSystem.exception;

// Thrown on signup if the email is already taken. Maps to HTTP 409.
public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
