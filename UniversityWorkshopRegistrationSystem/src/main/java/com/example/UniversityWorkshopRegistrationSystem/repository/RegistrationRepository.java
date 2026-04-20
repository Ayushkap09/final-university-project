package com.example.UniversityWorkshopRegistrationSystem.repository;

import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import com.example.UniversityWorkshopRegistrationSystem.model.User;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    // Check if user already has an ACTIVE registration for the workshop
    boolean existsByUserAndWorkshopAndStatus(User user, Workshop workshop, String status);
    List<Registration> findByUser(User user);
    List<Registration> findByWorkshop(Workshop workshop);
}
