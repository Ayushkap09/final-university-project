package com.example.UniversityWorkshopRegistrationSystem.repository;

import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
    // Get upcoming ACTIVE workshops for public listing
    List<Workshop> findByStatusAndStartDatetimeAfterOrderByStartDatetimeAsc(String status, LocalDateTime now);
}
