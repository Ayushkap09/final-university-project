package com.example.UniversityWorkshopRegistrationSystem.mapper;

import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopRequest;
import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopResponse;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkshopMapper {

    // Workshop -> WorkshopResponse
    WorkshopResponse toResponse(Workshop workshop);

    // List<Workshop> -> List<WorkshopResponse>
    List<WorkshopResponse> toResponseList(List<Workshop> workshops);

    // WorkshopRequest -> new Workshop (for create)
    Workshop toEntity(WorkshopRequest request);

    // Update an existing workshop entity from a request (for update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(WorkshopRequest request, @MappingTarget Workshop workshop);
}
