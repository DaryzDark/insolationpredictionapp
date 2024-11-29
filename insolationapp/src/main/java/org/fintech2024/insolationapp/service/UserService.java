package org.fintech2024.insolationapp.service;

import org.fintech2024.insolationapp.exeption.UserNotFoundException;
import org.fintech2024.insolationapp.model.User;
import org.fintech2024.insolationapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username
                + " not found"));
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User with this username already exists");
        }
        if ( userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with this email already exist");
        }
        return userRepository.save(user);
    }
}