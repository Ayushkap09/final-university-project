package com.example.UniversityWorkshopRegistrationSystem.service;

import com.example.UniversityWorkshopRegistrationSystem.dto.UserRegistrationRequest;
import com.example.UniversityWorkshopRegistrationSystem.exception.EmailAlreadyUsedException;
import com.example.UniversityWorkshopRegistrationSystem.exception.ResourceNotFoundException;
import com.example.UniversityWorkshopRegistrationSystem.model.User;
import com.example.UniversityWorkshopRegistrationSystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user as ATTENDEE. Password is hashed with BCrypt.
     */
    @Transactional
    public User register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException("Email is already registered: " + request.getEmail());
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("ATTENDEE");
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }
}
