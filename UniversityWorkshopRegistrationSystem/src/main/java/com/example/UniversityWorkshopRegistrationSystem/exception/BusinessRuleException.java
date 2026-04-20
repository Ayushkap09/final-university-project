package com.example.UniversityWorkshopRegistrationSystem.exception;

// Thrown when a business rule is violated:
// - sold out, duplicate registration, past workshop, already started, etc.
// Maps to HTTP 409 Conflict.
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
