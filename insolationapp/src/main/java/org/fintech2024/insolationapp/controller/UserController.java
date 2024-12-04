package org.fintech2024.insolationapp.controller;

import org.fintech2024.insolationapp.model.User;
import org.fintech2024.insolationapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    
}