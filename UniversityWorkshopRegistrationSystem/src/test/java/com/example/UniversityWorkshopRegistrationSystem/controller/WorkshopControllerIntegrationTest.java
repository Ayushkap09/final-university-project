package com.example.UniversityWorkshopRegistrationSystem.controller;

import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import com.example.UniversityWorkshopRegistrationSystem.repository.WorkshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test: full Spring context + MockMvc + H2.
 * Verifies the public workshop endpoints return ApiResponse JSON.
 */
@SpringBootTest
@AutoConfigureMockMvc
class WorkshopControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private WorkshopRepository workshopRepository;

    @BeforeEach
    void setUp() {
        workshopRepository.deleteAll();
        Workshop w = new Workshop();
        w.setTitle("Integration Test Workshop");
        w.setDescription("Testing");
        w.setLocation("Lab 1");
        w.setStartDatetime(LocalDateTime.now().plusDays(5));
        w.setTotalSeats(20);
        workshopRepository.save(w);
    }

    @Test
    void getWorkshops_returnsApiResponseWithList() throws Exception {
        mockMvc.perform(get("/api/v1/workshops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("Integration Test Workshop"));
    }

    @Test
    void getWorkshopById_notFound_returnsApiResponseError() throws Exception {
        mockMvc.perform(get("/api/v1/workshops/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }
}
