package org.fintech2024.insolationapp.repository;

import org.fintech2024.insolationapp.model.Request;
import org.fintech2024.insolationapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllRequestsByUser(User user);
}
