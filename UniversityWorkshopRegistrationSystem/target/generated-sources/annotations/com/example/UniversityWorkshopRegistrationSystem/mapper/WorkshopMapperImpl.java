package com.example.UniversityWorkshopRegistrationSystem.mapper;

import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopRequest;
import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopResponse;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T22:42:46-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class WorkshopMapperImpl implements WorkshopMapper {

    @Override
    public WorkshopResponse toResponse(Workshop workshop) {
        if ( workshop == null ) {
            return null;
        }

        WorkshopResponse workshopResponse = new WorkshopResponse();

        workshopResponse.setId( workshop.getId() );
        workshopResponse.setTitle( workshop.getTitle() );
        workshopResponse.setDescription( workshop.getDescription() );
        workshopResponse.setLocation( workshop.getLocation() );
        workshopResponse.setStartDatetime( workshop.getStartDatetime() );
        workshopResponse.setTotalSeats( workshop.getTotalSeats() );
        workshopResponse.setSeatsRemaining( workshop.getSeatsRemaining() );
        workshopResponse.setStatus( workshop.getStatus() );

        return workshopResponse;
    }

    @Override
    public List<WorkshopResponse> toResponseList(List<Workshop> workshops) {
        if ( workshops == null ) {
            return null;
        }

        List<WorkshopResponse> list = new ArrayList<WorkshopResponse>( workshops.size() );
        for ( Workshop workshop : workshops ) {
            list.add( toResponse( workshop ) );
        }

        return list;
    }

    @Override
    public Workshop toEntity(WorkshopRequest request) {
        if ( request == null ) {
            return null;
        }

        Workshop workshop = new Workshop();

        workshop.setTitle( request.getTitle() );
        workshop.setDescription( request.getDescription() );
        workshop.setLocation( request.getLocation() );
        workshop.setStartDatetime( request.getStartDatetime() );
        workshop.setTotalSeats( request.getTotalSeats() );

        return workshop;
    }

    @Override
    public void updateEntityFromRequest(WorkshopRequest request, Workshop workshop) {
        if ( request == null ) {
            return;
        }

        if ( request.getTitle() != null ) {
            workshop.setTitle( request.getTitle() );
        }
        if ( request.getDescription() != null ) {
            workshop.setDescription( request.getDescription() );
        }
        if ( request.getLocation() != null ) {
            workshop.setLocation( request.getLocation() );
        }
        if ( request.getStartDatetime() != null ) {
            workshop.setStartDatetime( request.getStartDatetime() );
        }
        workshop.setTotalSeats( request.getTotalSeats() );
    }
}
