package org.fintech2024.insolationapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с пользователем, который создал запрос
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Содержание запроса
    @Column(columnDefinition = "jsonb", nullable = false)
    private RequestContent content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    // Дата и время создания запроса
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Дата и время последнего обновления запроса
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Обработка времени создания и обновления
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = RequestStatus.NEW;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
