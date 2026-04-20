package com.example.UniversityWorkshopRegistrationSystem.service;

import com.example.UniversityWorkshopRegistrationSystem.exception.BusinessRuleException;
import com.example.UniversityWorkshopRegistrationSystem.exception.UnauthorizedException;
import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import com.example.UniversityWorkshopRegistrationSystem.model.User;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import com.example.UniversityWorkshopRegistrationSystem.repository.RegistrationRepository;
import com.example.UniversityWorkshopRegistrationSystem.repository.WorkshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RegistrationService.
 * Covers business rules A (sold out), B (duplicate), C (past), D (cancellation).
 */
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock private RegistrationRepository registrationRepository;
    @Mock private WorkshopRepository workshopRepository;
    @Mock private UserService userService;

    @InjectMocks private RegistrationService registrationService;

    private User user;
    private Workshop futureWorkshop;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("alice@u.edu");
        user.setName("Alice");
        user.setRole("ATTENDEE");

        futureWorkshop = new Workshop();
        futureWorkshop.setId(10L);
        futureWorkshop.setTitle("Spring Boot Basics");
        futureWorkshop.setLocation("Hall A");
        futureWorkshop.setStartDatetime(LocalDateTime.now().plusDays(7));
        futureWorkshop.setTotalSeats(10);
        futureWorkshop.setSeatsRemaining(5);
        futureWorkshop.setStatus("ACTIVE");
    }

    @Test
    void register_success_decrementsSeats() {
        when(userService.findByEmail("alice@u.edu")).thenReturn(user);
        when(workshopRepository.findById(10L)).thenReturn(Optional.of(futureWorkshop));
        when(registrationRepository.existsByUserAndWorkshopAndStatus(user, futureWorkshop, "ACTIVE")).thenReturn(false);
        when(registrationRepository.save(any(Registration.class))).thenAnswer(inv -> inv.getArgument(0));

        Registration result = registrationService.register("alice@u.edu", 10L);

        assertEquals("ACTIVE", result.getStatus());
        assertEquals(4, futureWorkshop.getSeatsRemaining(), "seats should decrement by 1");
        verify(workshopRepository).save(futureWorkshop);
    }

    @Test
    void register_soldOut_throwsBusinessRuleException() {
        futureWorkshop.setSeatsRemaining(0);
        when(userService.findByEmail("alice@u.edu")).thenReturn(user);
        when(workshopRepository.findById(10L)).thenReturn(Optional.of(futureWorkshop));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> registrationService.register("alice@u.edu", 10L));
        assertTrue(ex.getMessage().toLowerCase().contains("sold out"));
    }

    @Test
    void register_duplicate_throwsBusinessRuleException() {
        when(userService.findByEmail("alice@u.edu")).thenReturn(user);
        when(workshopRepository.findById(10L)).thenReturn(Optional.of(futureWorkshop));
        when(registrationRepository.existsByUserAndWorkshopAndStatus(user, futureWorkshop, "ACTIVE")).thenReturn(true);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> registrationService.register("alice@u.edu", 10L));
        assertTrue(ex.getMessage().toLowerCase().contains("already registered"));
    }

    @Test
    void register_pastWorkshop_throwsBusinessRuleException() {
        futureWorkshop.setStartDatetime(LocalDateTime.now().minusDays(1));
        when(userService.findByEmail("alice@u.edu")).thenReturn(user);
        when(workshopRepository.findById(10L)).thenReturn(Optional.of(futureWorkshop));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> registrationService.register("alice@u.edu", 10L));
        assertTrue(ex.getMessage().toLowerCase().contains("already started"));
    }

    @Test
    void register_cancelledWorkshop_throwsBusinessRuleException() {
        futureWorkshop.setStatus("CANCELLED");
        when(userService.findByEmail("alice@u.edu")).thenReturn(user);
        when(workshopRepository.findById(10L)).thenReturn(Optional.of(futureWorkshop));

        assertThrows(BusinessRuleException.class,
                () -> registrationService.register("alice@u.edu", 10L));
    }

    @Test
    void cancel_success_incrementsSeats() {
        Registration existing = new Registration();
        existing.setId(100L);
        existing.setUser(user);
        existing.setWorkshop(futureWorkshop);
        existing.setStatus("ACTIVE");

        when(registrationRepository.findById(100L)).thenReturn(Optional.of(existing));
        when(registrationRepository.save(any(Registration.class))).thenAnswer(inv -> inv.getArgument(0));

        Registration result = registrationService.cancel(100L, "alice@u.edu", false);

        assertEquals("CANCELLED", result.getStatus());
        assertNotNull(result.getCancelledAt());
        assertEquals(6, futureWorkshop.getSeatsRemaining(), "seats should increment by 1");
    }

    @Test
    void cancel_notOwner_throwsUnauthorized() {
        Registration existing = new Registration();
        existing.setId(100L);
        existing.setUser(user);
        existing.setWorkshop(futureWorkshop);
        existing.setStatus("ACTIVE");

        when(registrationRepository.findById(100L)).thenReturn(Optional.of(existing));

        assertThrows(UnauthorizedException.class,
                () -> registrationService.cancel(100L, "someone.else@u.edu", false));
    }

    @Test
    void cancel_workshopAlreadyStarted_throwsBusinessRuleException() {
        futureWorkshop.setStartDatetime(LocalDateTime.now().minusHours(1));
        Registration existing = new Registration();
        existing.setId(100L);
        existing.setUser(user);
        existing.setWorkshop(futureWorkshop);
        existing.setStatus("ACTIVE");

        when(registrationRepository.findById(100L)).thenReturn(Optional.of(existing));

        assertThrows(BusinessRuleException.class,
                () -> registrationService.cancel(100L, "alice@u.edu", false));
    }
}
