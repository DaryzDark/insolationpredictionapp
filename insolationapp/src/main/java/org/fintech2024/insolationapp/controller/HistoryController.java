package org.fintech2024.insolationapp.controller;

import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.Request;
import org.fintech2024.insolationapp.model.RequestContent;
import org.fintech2024.insolationapp.model.Response;
import org.fintech2024.insolationapp.model.User;
import org.fintech2024.insolationapp.service.RequestService;
import org.fintech2024.insolationapp.service.ResponseService;
import org.fintech2024.insolationapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/history")
public class HistoryController {

    private final RequestService requestService;
    private final ResponseService responseService;
    private final UserService userService;

    // Отображение страницы истории запросов
    @GetMapping
    public String showHistoryPage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        // Получение всех запросов пользователя
        List<Request> requests = requestService.getRequestsByUser(user);
        model.addAttribute("requests", requests);
        model.addAttribute("username", username);

        return "history"; // Шаблон history.html
    }

    // Обработка создания нового запроса
    @PostMapping("/create")
    public String createRequest(@RequestParam("content") RequestContent content, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        // Создание и обработка запроса
        Request request = requestService.createRequest(content, user);
        requestService.processRequest(request);

        return "redirect:/profile/history";
    }
}
