package com.example.UniversityWorkshopRegistrationSystem.web;

import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import com.example.UniversityWorkshopRegistrationSystem.service.RegistrationService;
import com.example.UniversityWorkshopRegistrationSystem.service.WorkshopService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeWebController {

    private final WorkshopService workshopService;
    private final RegistrationService registrationService;

    public HomeWebController(WorkshopService workshopService, RegistrationService registrationService) {
        this.workshopService = workshopService;
        this.registrationService = registrationService;
    }

    // Home = public list of upcoming workshops
    @GetMapping("/")
    public String home(Model model) {
        List<Workshop> workshops = workshopService.listUpcomingActive();
        model.addAttribute("workshops", workshops);
        return "home";
    }

    @GetMapping("/workshops/{id}")
    public String workshopDetail(@PathVariable Long id, Model model, Authentication auth) {
        Workshop workshop = workshopService.getById(id);
        model.addAttribute("workshop", workshop);

        Long existingRegistrationId = null;
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            List<Registration> myRegs = registrationService.getMyRegistrations(auth.getName());
            existingRegistrationId = myRegs.stream()
                    .filter(r -> r.getWorkshop().getId().equals(id) && "ACTIVE".equals(r.getStatus()))
                    .map(Registration::getId)
                    .findFirst()
                    .orElse(null);
        }
        model.addAttribute("existingRegistrationId", existingRegistrationId);
        return "workshops/detail";
    }
}
