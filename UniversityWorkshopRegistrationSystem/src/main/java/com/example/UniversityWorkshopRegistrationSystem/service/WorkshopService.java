package com.example.UniversityWorkshopRegistrationSystem.service;

import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopRequest;
import com.example.UniversityWorkshopRegistrationSystem.exception.ResourceNotFoundException;
import com.example.UniversityWorkshopRegistrationSystem.mapper.WorkshopMapper;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import com.example.UniversityWorkshopRegistrationSystem.repository.WorkshopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;

    public WorkshopService(WorkshopRepository workshopRepository, WorkshopMapper workshopMapper) {
        this.workshopRepository = workshopRepository;
        this.workshopMapper = workshopMapper;
    }

    // Public list - upcoming ACTIVE workshops only
    public List<Workshop> listUpcomingActive() {
        return workshopRepository.findByStatusAndStartDatetimeAfterOrderByStartDatetimeAsc(
                "ACTIVE", LocalDateTime.now());
    }

    // Admin sees every workshop
    public List<Workshop> listAll() {
        return workshopRepository.findAll();
    }

    public Workshop getById(Long id) {
        return workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found with id: " + id));
    }

    @Transactional
    public Workshop create(WorkshopRequest request) {
        Workshop workshop = workshopMapper.toEntity(request);
        // seatsRemaining is set to totalSeats via @PrePersist
        return workshopRepository.save(workshop);
    }

    @Transactional
    public Workshop update(Long id, WorkshopRequest request) {
        Workshop existing = getById(id);

        // Preserve seatsRemaining logic: if totalSeats is increased, add the delta to remaining
        int oldTotal = existing.getTotalSeats();
        int newTotal = request.getTotalSeats();
        int delta = newTotal - oldTotal;

        workshopMapper.updateEntityFromRequest(request, existing);

        if (delta != 0) {
            int newRemaining = existing.getSeatsRemaining() + delta;
            if (newRemaining < 0) newRemaining = 0;
            existing.setSeatsRemaining(newRemaining);
        }

        return workshopRepository.save(existing);
    }

    @Transactional
    public Workshop cancel(Long id) {
        Workshop workshop = getById(id);
        workshop.setStatus("CANCELLED");
        return workshopRepository.save(workshop);
    }
}
