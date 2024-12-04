package org.fintech2024.insolationapp.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.fintech2024.insolationapp.model.*;
import org.fintech2024.insolationapp.repository.RequestRepository;
import org.fintech2024.insolationapp.repository.ResponseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final ResponseRepository responseRepository;
    private final MlApiService externalApiService;

    public RequestService(RequestRepository requestRepository,
                          ResponseRepository responseRepository,
                          MlApiService externalApiService) {
        this.requestRepository = requestRepository;
        this.responseRepository = responseRepository;
        this.externalApiService = externalApiService;
    }

    @Transactional
    public Response createAndProcessRequest(RequestContent content, User user) {
        // Создание и сохранение запроса
        Request request = new Request();
        request.setContent(content);
        request.setUser(user);
        request.setStatus(RequestStatus.NEW);
        request = requestRepository.save(request);

        // Отправка запроса во внешнюю систему
        MlApiService.ExternalApiResponse externalResponse = externalApiService.sendRequest(content);

        // Создание и сохранение ответа
        Response response = new Response();
        response.setRequest(request);
        response.setContent(externalResponse.getContent());
        response.setStatusCode(externalResponse.getStatusCode());
        response = responseRepository.save(response);

        // Обновление статуса запроса
        request.setStatus(RequestStatus.COMPLETED);
        requestRepository.save(request);

        return response;
    }
}
