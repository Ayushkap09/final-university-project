package com.example.UniversityWorkshopRegistrationSystem.mapper;

import com.example.UniversityWorkshopRegistrationSystem.dto.AdminRegistrationResponse;
import com.example.UniversityWorkshopRegistrationSystem.dto.RegistrationResponse;
import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import com.example.UniversityWorkshopRegistrationSystem.model.User;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import java.time.LocalDateTime;
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
public class RegistrationMapperImpl implements RegistrationMapper {

    @Override
    public RegistrationResponse toResponse(Registration registration) {
        if ( registration == null ) {
            return null;
        }

        RegistrationResponse registrationResponse = new RegistrationResponse();

        registrationResponse.setWorkshopId( registrationWorkshopId( registration ) );
        registrationResponse.setWorkshopTitle( registrationWorkshopTitle( registration ) );
        registrationResponse.setWorkshopStartDatetime( registrationWorkshopStartDatetime( registration ) );
        registrationResponse.setWorkshopLocation( registrationWorkshopLocation( registration ) );
        registrationResponse.setId( registration.getId() );
        registrationResponse.setStatus( registration.getStatus() );
        registrationResponse.setCreatedAt( registration.getCreatedAt() );
        registrationResponse.setCancelledAt( registration.getCancelledAt() );

        return registrationResponse;
    }

    @Override
    public List<RegistrationResponse> toResponseList(List<Registration> registrations) {
        if ( registrations == null ) {
            return null;
        }

        List<RegistrationResponse> list = new ArrayList<RegistrationResponse>( registrations.size() );
        for ( Registration registration : registrations ) {
            list.add( toResponse( registration ) );
        }

        return list;
    }

    @Override
    public AdminRegistrationResponse toAdminResponse(Registration registration) {
        if ( registration == null ) {
            return null;
        }

        AdminRegistrationResponse adminRegistrationResponse = new AdminRegistrationResponse();

        adminRegistrationResponse.setRegistrationId( registration.getId() );
        adminRegistrationResponse.setParticipantName( registrationUserName( registration ) );
        adminRegistrationResponse.setParticipantEmail( registrationUserEmail( registration ) );
        adminRegistrationResponse.setRegisteredAt( registration.getCreatedAt() );
        adminRegistrationResponse.setStatus( registration.getStatus() );
        adminRegistrationResponse.setCancelledAt( registration.getCancelledAt() );

        return adminRegistrationResponse;
    }

    @Override
    public List<AdminRegistrationResponse> toAdminResponseList(List<Registration> registrations) {
        if ( registrations == null ) {
            return null;
        }

        List<AdminRegistrationResponse> list = new ArrayList<AdminRegistrationResponse>( registrations.size() );
        for ( Registration registration : registrations ) {
            list.add( toAdminResponse( registration ) );
        }

        return list;
    }

    private Long registrationWorkshopId(Registration registration) {
        if ( registration == null ) {
            return null;
        }
        Workshop workshop = registration.getWorkshop();
        if ( workshop == null ) {
            return null;
        }
        Long id = workshop.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String registrationWorkshopTitle(Registration registration) {
        if ( registration == null ) {
            return null;
        }
        Workshop workshop = registration.getWorkshop();
        if ( workshop == null ) {
            return null;
        }
        String title = workshop.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private LocalDateTime registrationWorkshopStartDatetime(Registration registration) {
        if ( registration == null ) {
            return null;
        }
        Workshop workshop = registration.getWorkshop();
        if ( workshop == null ) {
            return null;
        }
        LocalDateTime startDatetime = workshop.getStartDatetime();
        if ( startDatetime == null ) {
            return null;
        }
        return startDatetime;
    }

    private String registrationWorkshopLocation(Registration registration) {
        if ( registration == null ) {
            return null;
        }
        Workshop workshop = registration.getWorkshop();
        if ( workshop == null ) {
            return null;
        }
        String location = workshop.getLocation();
        if ( location == null ) {
            return null;
        }
        return location;
    }

    private String registrationUserName(Registration registration) {
        if ( registration == null ) {
            return null;
        }
        User user = registration.getUser();
        if ( user == null ) {
            return null;
        }
        String name = user.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String registrationUserEmail(Registration registration) {
        if ( registration == null ) {
            return null;
        }
        User user = registration.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }
}
