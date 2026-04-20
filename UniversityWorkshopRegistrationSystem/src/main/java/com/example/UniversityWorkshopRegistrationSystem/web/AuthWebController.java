package com.example.UniversityWorkshopRegistrationSystem.web;

import com.example.UniversityWorkshopRegistrationSystem.dto.UserRegistrationRequest;
import com.example.UniversityWorkshopRegistrationSystem.exception.EmailAlreadyUsedException;
import com.example.UniversityWorkshopRegistrationSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthWebController {

    private final UserService userService;

    public AuthWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new UserRegistrationRequest());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("userForm") UserRegistrationRequest form,
                             BindingResult bindingResult,
                             RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.register(form);
        } catch (EmailAlreadyUsedException e) {
            bindingResult.rejectValue("email", "email.used", e.getMessage());
            return "auth/register";
        }
        redirect.addFlashAttribute("flashSuccess", "Account created. Please log in.");
        return "redirect:/login";
    }
}
