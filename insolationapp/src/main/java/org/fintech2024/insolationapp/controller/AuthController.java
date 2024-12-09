package org.fintech2024.insolationapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.*;
import org.fintech2024.insolationapp.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("signUpRequest") @Valid SignUpRequest request,
                         BindingResult result,
                         Model model,
                         HttpServletResponse response) {
        if (result.hasErrors()) {
            return "sign-up";
        }
        try {
            // Регистрация пользователя и установка JWT в cookies
            authenticationService.signUp(request, response);
            return "sign-upSuccess";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "sign-up";
        }
    }

    @GetMapping("/sign-in")
    public String showSignInForm(Model model) {
        model.addAttribute("signInRequest", new SignInRequest());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("signInRequest") @Valid SignInRequest request,
                         BindingResult result,
                         Model model,
                         HttpServletResponse response) {
        if (result.hasErrors()) {
            return "sign-in";
        }
        try {
            // Аутентификация пользователя и установка JWT в cookies
            authenticationService.signIn(request, response);
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "sign-in";
        }
    }

    @GetMapping("/logout-success")
    public String logoutPage(HttpServletResponse response) {
        return "logout";
    }
}