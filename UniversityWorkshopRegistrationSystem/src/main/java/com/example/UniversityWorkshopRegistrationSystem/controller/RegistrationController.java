package com.example.UniversityWorkshopRegistrationSystem.controller;

import com.example.UniversityWorkshopRegistrationSystem.api.ApiResponse;
import com.example.UniversityWorkshopRegistrationSystem.dto.RegistrationResponse;
import com.example.UniversityWorkshopRegistrationSystem.mapper.RegistrationMapper;
import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import com.example.UniversityWorkshopRegistrationSystem.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Registration endpoints for authenticated users.
 */
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final RegistrationMapper registrationMapper;

    public RegistrationController(RegistrationService registrationService, RegistrationMapper registrationMapper) {
        this.registrationService = registrationService;
        this.registrationMapper = registrationMapper;
    }

    // Register the logged-in user for a workshop
    @PostMapping("/workshops/{id}/registrations")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@PathVariable Long id,
                                                                      Authentication authentication) {
        Registration reg = registrationService.register(authentication.getName(), id);
        RegistrationResponse data = registrationMapper.toResponse(reg);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registered successfully", data));
    }

    // Cancel a registration (must belong to user, unless ADMIN)
    @DeleteMapping("/registrations/{registrationId}")
    public ResponseEntity<ApiResponse<RegistrationResponse>> cancel(@PathVariable Long registrationId,
                                                                    Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
        Registration reg = registrationService.cancel(registrationId, authentication.getName(), isAdmin);
        RegistrationResponse data = registrationMapper.toResponse(reg);
        return ResponseEntity.ok(ApiResponse.success("Registration cancelled", data));
    }

    // List the logged-in user's registrations
    @GetMapping("/me/registrations")
    public ResponseEntity<ApiResponse<List<RegistrationResponse>>> myRegistrations(Authentication authentication) {
        List<RegistrationResponse> data = registrationMapper
                .toResponseList(registrationService.getMyRegistrations(authentication.getName()));
        return ResponseEntity.ok(ApiResponse.success("My registrations fetched", data));
    }
}
