package org.fintech2024.insolationapp.service;

import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.*;
import org.fintech2024.insolationapp.repository.RequestRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final ResponseService responseService;
    private final MlApiService mlApiService;


    public List<Request> getRequestsByUser(User user) {
        return requestRepository.findAllRequestsByUser(user);
    }

    @Transactional
    public Request createRequest(String requestName, RequestContent content, User user) {
        Request request = new Request();
        request.setRequestName(requestName);
        request.setContent(content);
        request.setUser(user);
        request.setStatus(RequestStatus.NEW);
        return requestRepository.save(request);
    }

    @Async
    @Transactional
    public void processRequest(Request request) {
        try {
            Thread.sleep(2000);
            request.setStatus(RequestStatus.PROCESSING);
            Thread.sleep(10000);
            ExternalApiResponse externalApiResponse = mlApiService.sendRequest(request.getContent());
            var response = Response.builder()
                            .request(request)
                            .content(externalApiResponse.getContent())
                            .statusCode(externalApiResponse.getStatusCode())
                            .request(request)
                            .build();
            responseService.saveResponse(response);

            if (externalApiResponse.getStatusCode() == 200) {
                request.setStatus(RequestStatus.COMPLETED);
            } else {
                request.setStatus(RequestStatus.ERROR);
            }
        } catch (Exception e) {
            // Обновление статуса запроса при ошибке
            request.setStatus(RequestStatus.FAILED);
        }

        request.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(request);
    }
}
