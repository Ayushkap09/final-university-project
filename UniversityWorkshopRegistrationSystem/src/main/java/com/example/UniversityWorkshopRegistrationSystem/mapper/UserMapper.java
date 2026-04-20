package com.example.UniversityWorkshopRegistrationSystem.mapper;

import com.example.UniversityWorkshopRegistrationSystem.dto.UserResponse;
import com.example.UniversityWorkshopRegistrationSystem.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
