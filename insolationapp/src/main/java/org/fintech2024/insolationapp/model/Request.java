package org.fintech2024.insolationapp.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String requestName;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private RequestContent content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
