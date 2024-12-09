package org.fintech2024.insolationapp.controller;

import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.User;
import org.fintech2024.insolationapp.service.AuthenticationService;
import org.fintech2024.insolationapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public String showSettingsPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("title", "Настройки");
        model.addAttribute("page", "settings");
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping("/update")
    public String updateSettings(@RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(userDetails.getUsername());
            authenticationService.updateUserPassword(user, password, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Настройки успешно обновлены.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile/settings";
    }
}
