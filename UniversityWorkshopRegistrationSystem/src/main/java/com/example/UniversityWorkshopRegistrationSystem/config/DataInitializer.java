package com.example.UniversityWorkshopRegistrationSystem.config;

import com.example.UniversityWorkshopRegistrationSystem.model.User;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import com.example.UniversityWorkshopRegistrationSystem.repository.UserRepository;
import com.example.UniversityWorkshopRegistrationSystem.repository.WorkshopRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Seeds a default admin + a couple of sample workshops on first run
 * so the app is usable out of the box.
 *
 * Default admin:   admin@university.edu / Admin@123
 * Default user:    attendee@university.edu / Attend@123
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepo,
                                      WorkshopRepository workshopRepo,
                                      PasswordEncoder encoder) {
        return args -> {
            if (!userRepo.existsByEmail("admin@university.edu")) {
                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@university.edu");
                admin.setPasswordHash(encoder.encode("Admin@123"));
                admin.setRole("ADMIN");
                userRepo.save(admin);
            }

            if (!userRepo.existsByEmail("attendee@university.edu")) {
                User attendee = new User();
                attendee.setName("Test Attendee");
                attendee.setEmail("attendee@university.edu");
                attendee.setPasswordHash(encoder.encode("Attend@123"));
                attendee.setRole("ATTENDEE");
                userRepo.save(attendee);
            }

            if (workshopRepo.count() == 0) {
                Workshop w1 = new Workshop();
                w1.setTitle("Spring Boot Basics");
                w1.setDescription("Learn the fundamentals of Spring Boot, including REST APIs, JPA, and dependency injection.");
                w1.setLocation("Main Campus Hall A");
                w1.setStartDatetime(LocalDateTime.now().plusDays(14));
                w1.setTotalSeats(30);
                workshopRepo.save(w1);

                Workshop w2 = new Workshop();
                w2.setTitle("Career Fair Prep");
                w2.setDescription("Resume reviews, mock interviews, and networking tips for the upcoming career fair.");
                w2.setLocation("Student Center Room 204");
                w2.setStartDatetime(LocalDateTime.now().plusDays(20));
                w2.setTotalSeats(20);
                workshopRepo.save(w2);

                Workshop w3 = new Workshop();
                w3.setTitle("Intro to Databases");
                w3.setDescription("Covers SQL basics, relational modeling, and normalization.");
                w3.setLocation("Tech Building Lab 3");
                w3.setStartDatetime(LocalDateTime.now().plusDays(30));
                w3.setTotalSeats(25);
                workshopRepo.save(w3);
            }
        };
    }
}
