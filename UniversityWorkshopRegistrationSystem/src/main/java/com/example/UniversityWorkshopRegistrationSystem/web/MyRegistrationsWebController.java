package com.example.UniversityWorkshopRegistrationSystem.web;

import com.example.UniversityWorkshopRegistrationSystem.exception.BusinessRuleException;
import com.example.UniversityWorkshopRegistrationSystem.exception.ResourceNotFoundException;
import com.example.UniversityWorkshopRegistrationSystem.exception.UnauthorizedException;
import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import com.example.UniversityWorkshopRegistrationSystem.service.RegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MyRegistrationsWebController {

    private final RegistrationService registrationService;

    public MyRegistrationsWebController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/my/registrations")
    public String myRegistrations(Authentication auth, Model model) {
        List<Registration> regs = registrationService.getMyRegistrations(auth.getName());
        model.addAttribute("registrations", regs);
        return "my/registrations";
    }

    // Register for a workshop (triggered from the workshop detail page)
    @PostMapping("/workshops/{id}/register")
    public String register(@PathVariable Long id, Authentication auth, RedirectAttributes redirect) {
        try {
            registrationService.register(auth.getName(), id);
            redirect.addFlashAttribute("flashSuccess", "Successfully registered for the workshop!");
        } catch (BusinessRuleException | ResourceNotFoundException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/workshops/" + id;
    }

    // Cancel a registration (from My Registrations page)
    @PostMapping("/my/registrations/{registrationId}/cancel")
    public String cancel(@PathVariable Long registrationId, Authentication auth, RedirectAttributes redirect) {
        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
        try {
            registrationService.cancel(registrationId, auth.getName(), isAdmin);
            redirect.addFlashAttribute("flashSuccess", "Registration cancelled.");
        } catch (BusinessRuleException | ResourceNotFoundException | UnauthorizedException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/my/registrations";
    }
}
