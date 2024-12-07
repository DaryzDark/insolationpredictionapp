package org.fintech2024.insolationapp.model;

import lombok.Data;

@Data
public class ExternalApiResponse {
    private ResponseContent content;
    private int statusCode;
}
