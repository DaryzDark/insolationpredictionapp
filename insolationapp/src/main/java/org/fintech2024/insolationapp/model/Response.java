package org.fintech2024.insolationapp.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

    @OneToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private ResponseContent content;

    @Column(name = "status_code", nullable = false)
    private int statusCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
