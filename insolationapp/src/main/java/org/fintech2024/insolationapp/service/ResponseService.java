package org.fintech2024.insolationapp.service;

import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.Request;
import org.fintech2024.insolationapp.model.Response;
import org.fintech2024.insolationapp.repository.ResponseRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;

    // Сохранение ответа
    public void saveResponse(Response response) {
        responseRepository.save(response);
    }

    public Map<Long, Response> getResponsesForRequests(List<Request> requests) {
        Map<Long, Response> responses = new HashMap<>();
        for (Request request : requests) {
            Response response = getResponseByRequestId(request.getId());
            if (response != null) {
                responses.put(request.getId(), response);
            }
        }
        return responses;
    }

    // Получение ответа по запросу
    public Response getResponseByRequestId(Long requestId) {
        return responseRepository.findByRequestId(requestId)
                .orElse(null);
    }
}
