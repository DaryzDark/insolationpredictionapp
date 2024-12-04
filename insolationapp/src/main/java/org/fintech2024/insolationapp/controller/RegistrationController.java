package org.fintech2024.insolationapp.controller;

import jakarta.validation.Valid;
import org.fintech2024.insolationapp.model.User;
import org.fintech2024.insolationapp.model.UserRegistration;
import org.fintech2024.insolationapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistration registration) {
        try {
            User user = new User();
            user.setUsername(registration.getUsername());
            user.setPassword(registration.getPassword());
            user.setEmail(registration.getEmail());
            user.setFullName(registration.getFullName());

            userService.createUser(user);

            return ResponseEntity.ok("Пользователь успешно зарегистрирован");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
