package org.fintech2024.insolationapp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.*;
import org.fintech2024.insolationapp.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final ResponseService responseService;
    private final MlApiService mlApiService;


    // Получение всех запросов пользователя
    public List<Request> getRequestsByUser(User user) {
        return requestRepository.findAllRequestsByUser(user);
    }

    // Создание нового запроса
    public Request createRequest(RequestContent content, User user) {
        Request request = new Request();
        request.setContent(content);
        request.setUser(user);
        request.setStatus(RequestStatus.NEW);
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        return requestRepository.save(request);
    }

    // Обработка запроса: отправка во внешнюю систему
    public void processRequest(Request request) {
        try {
            // Отправка запроса во внешнюю систему
            ExternalApiResponse externalApiResponse = mlApiService.sendRequest(request.getContent());

            var response = Response.builder()
                            .request(request)
                            .content(externalApiResponse.getContent())
                            .statusCode(externalApiResponse.getStatusCode())
                            .request(request)
                            .build();
            responseService.saveResponse(response);

            // Обновление статуса запроса
            request.setStatus(RequestStatus.COMPLETED);
        } catch (Exception e) {
            // Обновление статуса запроса при ошибке
            request.setStatus(RequestStatus.FAILED);
        }

        request.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(request);
    }
}
