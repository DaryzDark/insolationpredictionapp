package org.fintech2024.insolationapp.service;

import org.fintech2024.insolationapp.exeption.UserAlreadyExistsException;
import org.fintech2024.insolationapp.exeption.UserNotFoundException;
import org.fintech2024.insolationapp.model.Role;
import org.fintech2024.insolationapp.model.User;
import org.fintech2024.insolationapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username was already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email was already taken");
        }
        user.setActive(true);
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));
    }
    public UserDetailsService userDetailsService() {
        return this::getUserByUsername;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void activateUser(Long id) {
        User user = getUserById(id);
        user.setActive(true);
        userRepository.save(user);
    }

    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}