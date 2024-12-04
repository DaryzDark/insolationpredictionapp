package org.fintech2024.insolationapp.service;

import lombok.Data;
import org.fintech2024.insolationapp.model.RequestContent;
import org.fintech2024.insolationapp.model.ResponseContent;
import org.springframework.stereotype.Service;

@Service
public class MlApiService {

    public ExternalApiResponse sendRequest(RequestContent content) {
        // Генерация фиктивного ответа без обращения к внешнему API
        ResponseContent responseContent = new ResponseContent();
        responseContent.setExampleResponseContent("Это сгенерированный ответ на ваш запрос с данными: " + content.getExampleRequestContent());

        int statusCode = 200; // Успешный код ответа

        // Возвращаем обёртку с данными ответа
        ExternalApiResponse externalApiResponse = new ExternalApiResponse();
        externalApiResponse.setContent(responseContent);
        externalApiResponse.setStatusCode(statusCode);

        return externalApiResponse;
    }

    @Data
    public static class ExternalApiResponse {
        private ResponseContent content;
        private int statusCode;
    }
}
