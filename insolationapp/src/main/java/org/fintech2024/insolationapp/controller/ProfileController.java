package org.fintech2024.insolationapp.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping
    public String showProfilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("title", "Личный кабинет");
        model.addAttribute("page", "profile");
        return "profile";
    }
}
