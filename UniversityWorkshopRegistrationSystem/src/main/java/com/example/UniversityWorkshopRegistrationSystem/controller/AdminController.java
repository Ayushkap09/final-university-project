package com.example.UniversityWorkshopRegistrationSystem.controller;

import com.example.UniversityWorkshopRegistrationSystem.api.ApiResponse;
import com.example.UniversityWorkshopRegistrationSystem.dto.AdminRegistrationResponse;
import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopRequest;
import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopResponse;
import com.example.UniversityWorkshopRegistrationSystem.mapper.RegistrationMapper;
import com.example.UniversityWorkshopRegistrationSystem.mapper.WorkshopMapper;
import com.example.UniversityWorkshopRegistrationSystem.service.RegistrationService;
import com.example.UniversityWorkshopRegistrationSystem.service.WorkshopService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin-only endpoints. Security config restricts these to ROLE_ADMIN.
 */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final WorkshopService workshopService;
    private final RegistrationService registrationService;
    private final WorkshopMapper workshopMapper;
    private final RegistrationMapper registrationMapper;

    public AdminController(WorkshopService workshopService,
                           RegistrationService registrationService,
                           WorkshopMapper workshopMapper,
                           RegistrationMapper registrationMapper) {
        this.workshopService = workshopService;
        this.registrationService = registrationService;
        this.workshopMapper = workshopMapper;
        this.registrationMapper = registrationMapper;
    }

    @PostMapping("/workshops")
    public ResponseEntity<ApiResponse<WorkshopResponse>> create(@Valid @RequestBody WorkshopRequest request) {
        WorkshopResponse data = workshopMapper.toResponse(workshopService.create(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Workshop created", data));
    }

    @PutMapping("/workshops/{id}")
    public ResponseEntity<ApiResponse<WorkshopResponse>> update(@PathVariable Long id,
                                                                @Valid @RequestBody WorkshopRequest request) {
        WorkshopResponse data = workshopMapper.toResponse(workshopService.update(id, request));
        return ResponseEntity.ok(ApiResponse.success("Workshop updated", data));
    }

    @PatchMapping("/workshops/{id}/cancel")
    public ResponseEntity<ApiResponse<WorkshopResponse>> cancel(@PathVariable Long id) {
        WorkshopResponse data = workshopMapper.toResponse(workshopService.cancel(id));
        return ResponseEntity.ok(ApiResponse.success("Workshop cancelled", data));
    }

    @GetMapping("/workshops/{id}/registrations")
    public ResponseEntity<ApiResponse<List<AdminRegistrationResponse>>> getRegistrations(@PathVariable Long id) {
        List<AdminRegistrationResponse> data = registrationMapper
                .toAdminResponseList(registrationService.getByWorkshop(id));
        return ResponseEntity.ok(ApiResponse.success("Registrations fetched", data));
    }
}
