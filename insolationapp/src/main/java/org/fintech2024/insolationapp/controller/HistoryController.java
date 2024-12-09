package org.fintech2024.insolationapp.controller;

import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.Request;
import org.fintech2024.insolationapp.model.RequestContent;
import org.fintech2024.insolationapp.model.Response;
import org.fintech2024.insolationapp.service.RequestService;
import org.fintech2024.insolationapp.service.ResponseService;
import org.fintech2024.insolationapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final UserService userService;
    private final RequestService requestService;
    private final ResponseService responseService;

    @GetMapping
    public String showHistoryPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Request> requests = requestService.getRequestsByUser(userService.getUserByUsername(userDetails.getUsername()));
        Map<Long, Response> responses = responseService.getResponsesForRequests(requests);
        model.addAttribute("title", "История запросов");
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("page", "history");
        model.addAttribute("requests", requests);
        model.addAttribute("responses", responses);
        return "history";
    }

    @PostMapping("/create")
    public String createRequest(@RequestParam String requestName,
                                @RequestParam String exampleRequestContent,
                                @AuthenticationPrincipal UserDetails userDetails) {
        RequestContent content = new RequestContent();
        content.setExampleRequestContent(exampleRequestContent);

        Request request = requestService.createRequest(requestName, content, userService.getUserByUsername(userDetails.getUsername()));
        requestService.processRequest(request);
        return "redirect:/history";
    }
}
