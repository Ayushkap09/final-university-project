package com.example.UniversityWorkshopRegistrationSystem.web;

import com.example.UniversityWorkshopRegistrationSystem.dto.WorkshopRequest;
import com.example.UniversityWorkshopRegistrationSystem.exception.ResourceNotFoundException;
import com.example.UniversityWorkshopRegistrationSystem.model.Registration;
import com.example.UniversityWorkshopRegistrationSystem.model.Workshop;
import com.example.UniversityWorkshopRegistrationSystem.service.RegistrationService;
import com.example.UniversityWorkshopRegistrationSystem.service.WorkshopService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    private final WorkshopService workshopService;
    private final RegistrationService registrationService;

    public AdminWebController(WorkshopService workshopService, RegistrationService registrationService) {
        this.workshopService = workshopService;
        this.registrationService = registrationService;
    }

    @GetMapping("/workshops")
    public String list(Model model) {
        List<Workshop> workshops = workshopService.listAll();
        model.addAttribute("workshops", workshops);
        return "admin/workshops";
    }

    @GetMapping("/workshops/new")
    public String newForm(Model model) {
        if (!model.containsAttribute("workshopForm")) {
            model.addAttribute("workshopForm", new WorkshopRequest());
        }
        model.addAttribute("isEdit", false);
        return "admin/workshop-form";
    }

    @PostMapping("/workshops")
    public String create(@Valid @ModelAttribute("workshopForm") WorkshopRequest form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin/workshop-form";
        }
        workshopService.create(form);
        redirect.addFlashAttribute("flashSuccess", "Workshop created successfully.");
        return "redirect:/admin/workshops";
    }

    @GetMapping("/workshops/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Workshop w = workshopService.getById(id);
        WorkshopRequest form = new WorkshopRequest();
        form.setTitle(w.getTitle());
        form.setDescription(w.getDescription());
        form.setLocation(w.getLocation());
        form.setStartDatetime(w.getStartDatetime());
        form.setTotalSeats(w.getTotalSeats());
        model.addAttribute("workshopForm", form);
        model.addAttribute("workshopId", id);
        model.addAttribute("isEdit", true);
        return "admin/workshop-form";
    }

    @PostMapping("/workshops/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("workshopForm") WorkshopRequest form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("workshopId", id);
            model.addAttribute("isEdit", true);
            return "admin/workshop-form";
        }
        workshopService.update(id, form);
        redirect.addFlashAttribute("flashSuccess", "Workshop updated successfully.");
        return "redirect:/admin/workshops";
    }

    @PostMapping("/workshops/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            workshopService.cancel(id);
            redirect.addFlashAttribute("flashSuccess", "Workshop cancelled.");
        } catch (ResourceNotFoundException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/workshops";
    }

    @GetMapping("/workshops/{id}/registrations")
    public String registrations(@PathVariable Long id, Model model) {
        Workshop w = workshopService.getById(id);
        List<Registration> regs = registrationService.getByWorkshop(id);
        model.addAttribute("workshop", w);
        model.addAttribute("registrations", regs);
        return "admin/workshop-registrations";
    }
}
