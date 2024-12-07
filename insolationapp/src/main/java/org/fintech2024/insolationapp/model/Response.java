package org.fintech2024.insolationapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "responses")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с запросом, на который был получен ответ
    @OneToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    // Содержание ответа
    @Column(columnDefinition = "jsonb", nullable = false)
    private ResponseContent content;

    // HTTP-статус ответа
    @Column(name = "status_code", nullable = false)
    private int statusCode;

    // Дата и время получения ответа
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
