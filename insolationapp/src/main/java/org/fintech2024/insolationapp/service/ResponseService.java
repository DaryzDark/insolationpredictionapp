package org.fintech2024.insolationapp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.Request;
import org.fintech2024.insolationapp.model.Response;
import org.fintech2024.insolationapp.repository.ResponseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;

    // Сохранение ответа
    public void saveResponse(Response response) {
        responseRepository.save(response);
    }

    // Получение ответа по запросу
    public Response getResponseByRequestId(Long requestId) {
        return responseRepository.findByRequestId(requestId)
                .orElseThrow(() -> new RuntimeException("Ответ не найден"));
    }
}
