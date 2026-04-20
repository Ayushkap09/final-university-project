package com.example.UniversityWorkshopRegistrationSystem.mapper;

import com.example.UniversityWorkshopRegistrationSystem.dto.AdminRegistrationResponse;
import com.example.UniversityWorkshopRegistrationSystem.dto.RegistrationResponse;
import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    // Registration -> RegistrationResponse (for the attendee view)
    @Mapping(source = "workshop.id", target = "workshopId")
    @Mapping(source = "workshop.title", target = "workshopTitle")
    @Mapping(source = "workshop.startDatetime", target = "workshopStartDatetime")
    @Mapping(source = "workshop.location", target = "workshopLocation")
    RegistrationResponse toResponse(Registration registration);

    List<RegistrationResponse> toResponseList(List<Registration> registrations);

    // Registration -> AdminRegistrationResponse (admin sees participant info)
    @Mapping(source = "id", target = "registrationId")
    @Mapping(source = "user.name", target = "participantName")
    @Mapping(source = "user.email", target = "participantEmail")
    @Mapping(source = "createdAt", target = "registeredAt")
    AdminRegistrationResponse toAdminResponse(Registration registration);

    List<AdminRegistrationResponse> toAdminResponseList(List<Registration> registrations);
}
