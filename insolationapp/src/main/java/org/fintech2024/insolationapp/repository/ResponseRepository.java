package org.fintech2024.insolationapp.repository;

import org.fintech2024.insolationapp.model.Request;
import org.fintech2024.insolationapp.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    Optional<Response> findByRequestId(Long requestId);
}
