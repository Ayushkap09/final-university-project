package com.example.UniversityWorkshopRegistrationSystem.controller;

import com.example.UniversityWorkshopRegistrationSystem.api.ApiResponse;
import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopResponse;
import com.example.UniversityWorkshopRegistrationSystem.mapper.WorkshopMapper;
import com.example.UniversityWorkshopRegistrationSystem.service.WorkshopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public workshop endpoints - no login required.
 */
@RestController
@RequestMapping("/api/v1/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;
    private final WorkshopMapper workshopMapper;

    public WorkshopController(WorkshopService workshopService, WorkshopMapper workshopMapper) {
        this.workshopService = workshopService;
        this.workshopMapper = workshopMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkshopResponse>>> listAll() {
        List<WorkshopResponse> data = workshopMapper.toResponseList(workshopService.listUpcomingActive());
        return ResponseEntity.ok(ApiResponse.success("Workshops fetched successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkshopResponse>> getById(@PathVariable Long id) {
        WorkshopResponse data = workshopMapper.toResponse(workshopService.getById(id));
        return ResponseEntity.ok(ApiResponse.success("Workshop fetched successfully", data));
    }
}
