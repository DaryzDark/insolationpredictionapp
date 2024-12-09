package org.fintech2024.insolationapp.service;

import org.fintech2024.insolationapp.exeption.UserAlreadyExistsException;
import org.fintech2024.insolationapp.exeption.UserNotFoundException;
import org.fintech2024.insolationapp.model.Role;
import org.fintech2024.insolationapp.model.User;
import org.fintech2024.insolationapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testCreateUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        // Act
        userService.createUser(user);

        // Assert
        Mockito.verify(userRepository).save(user);
        assertTrue(user.getActive());
        assertEquals(Role.ROLE_USER, user.getRole());
    }

    @Test
    void testCreateUser_UsernameAlreadyTaken() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testCreateUser_EmailAlreadyTaken() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(user.getId());

        // Assert
        assertEquals(user, result);
        Mockito.verify(userRepository).findById(user.getId());
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        Mockito.verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserByUsername_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByUsername(user.getUsername());

        // Assert
        assertEquals(user, result);
        Mockito.verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        // Arrange
        String username = "testUser";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(username));
        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void testActivateUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setActive(false);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        userService.activateUser(user.getId());

        // Assert
        assertTrue(user.getActive());
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void testDeactivateUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setActive(true);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        userService.deactivateUser(user.getId());

        // Assert
        assertFalse(user.getActive());
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(new User(), new User());

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void testSaveUser() {
        User user = new User();

        userService.save(user);

        Mockito.verify(userRepository).save(user);
    }
}
