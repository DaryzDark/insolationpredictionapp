package org.fintech2024.insolationapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.exeption.UserAlreadyExistsException;
import org.fintech2024.insolationapp.model.*;
import org.fintech2024.insolationapp.service.AuthenticationService;
import org.fintech2024.insolationapp.service.UserService;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("signUpRequest") @Valid SignUpRequest request,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return "sign-up";
        }
        try {
            authenticationService.signUp(request);
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
            AuthenticationResponse authResponse = authenticationService.signIn(request);

            // Установка JWT в HTTP-only Cookie
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", authResponse.getToken())
                    .httpOnly(true) // Защищённое Cookie
                    .secure(false)   // Только через HTTPS
                    .path("/")      // Доступно для всех путей
                    .maxAge(24 * 60 * 60) // Срок действия: 1 день
                    .sameSite("Strict") // Политика SameSite
                    .build();

            response.addHeader("Set-Cookie", jwtCookie.toString());
            return "redirect:/profile/history";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "sign-in";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response, Model model) {
        // Удаление Cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0) // Удаление Cookie
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());
        model.addAttribute("message", "Вы успешно вышли из системы.");
        return "redirect:/auth/sign-in";
    }
}