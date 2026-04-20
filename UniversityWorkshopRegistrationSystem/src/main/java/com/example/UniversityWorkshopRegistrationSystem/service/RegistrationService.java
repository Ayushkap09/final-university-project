package com.example.UniversityWorkshopRegistrationSystem.service;

import com.example.UniversityWorkshopRegistrationSystem.exception.BusinessRuleException;
import com.example.UniversityWorkshopRegistrationSystem.exception.ResourceNotFoundException;
import com.example.UniversityWorkshopRegistrationSystem.exception.UnauthorizedException;
import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import com.example.UniversityWorkshopRegistrationSystem.model.User;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import com.example.UniversityWorkshopRegistrationSystem.repository.RegistrationRepository;
import com.example.UniversityWorkshopRegistrationSystem.repository.WorkshopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Holds ALL registration business rules (Rules A, B, C, D from spec).
 * Controllers must NOT duplicate any of these checks.
 */
@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final WorkshopRepository workshopRepository;
    private final UserService userService;

    public RegistrationService(RegistrationRepository registrationRepository,
                               WorkshopRepository workshopRepository,
                               UserService userService) {
        this.registrationRepository = registrationRepository;
        this.workshopRepository = workshopRepository;
        this.userService = userService;
    }

    /**
     * Registers a user for a workshop.
     * Enforces Rules A (sold out), B (duplicate), C (past).
     * Runs inside a transaction.
     */
    @Transactional
    public Registration register(String userEmail, Long workshopId) {
        User user = userService.findByEmail(userEmail);

        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found with id: " + workshopId));

        // Rule: cannot register for cancelled workshop
        if ("CANCELLED".equals(workshop.getStatus())) {
            throw new BusinessRuleException("This workshop has been cancelled");
        }

        // Rule C - no registration for past workshops
        if (workshop.getStartDatetime().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Cannot register for a workshop that has already started");
        }

        // Rule A - sold out
        if (workshop.getSeatsRemaining() <= 0) {
            throw new BusinessRuleException("Workshop is sold out");
        }

        // Rule B - no duplicate active registration
        if (registrationRepository.existsByUserAndWorkshopAndStatus(user, workshop, "ACTIVE")) {
            throw new BusinessRuleException("You are already registered for this workshop");
        }

        // Create registration
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setWorkshop(workshop);
        registration.setStatus("ACTIVE");

        // Decrement seats
        workshop.setSeatsRemaining(workshop.getSeatsRemaining() - 1);
        workshopRepository.save(workshop);

        return registrationRepository.save(registration);
    }

    /**
     * Cancels a registration.
     * Enforces Rule D (cannot cancel after workshop starts) and ownership check.
     */
    @Transactional
    public Registration cancel(Long registrationId, String userEmail, boolean isAdmin) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + registrationId));

        // Ownership check (unless admin)
        if (!isAdmin && !registration.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You cannot cancel someone else's registration");
        }

        // Already cancelled?
        if ("CANCELLED".equals(registration.getStatus())) {
            throw new BusinessRuleException("Registration is already cancelled");
        }

        // Rule D - cannot cancel after workshop has started
        if (registration.getWorkshop().getStartDatetime().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Cannot cancel a registration for a workshop that has already started");
        }

        // Mark cancelled
        registration.setStatus("CANCELLED");
        registration.setCancelledAt(LocalDateTime.now());

        // Give the seat back
        Workshop workshop = registration.getWorkshop();
        workshop.setSeatsRemaining(workshop.getSeatsRemaining() + 1);
        workshopRepository.save(workshop);

        return registrationRepository.save(registration);
    }

    public List<Registration> getMyRegistrations(String userEmail) {
        User user = userService.findByEmail(userEmail);
        return registrationRepository.findByUser(user);
    }

    public List<Registration> getByWorkshop(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found with id: " + workshopId));
        return registrationRepository.findByWorkshop(workshop);
    }
}
