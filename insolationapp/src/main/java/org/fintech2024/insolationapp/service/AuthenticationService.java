package org.fintech2024.insolationapp.service;

import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.AuthenticationResponse;
import org.fintech2024.insolationapp.model.Role;
import org.fintech2024.insolationapp.model.SignInRequest;
import org.fintech2024.insolationapp.model.SignUpRequest;
import org.fintech2024.insolationapp.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.fintech2024.insolationapp.model.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse signUp(SignUpRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.ROLE_USER)
                .build();

        userService.createUser(user);

        var jwt = jwtService.generateToken(user, false);
        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user, request.isRememberMe());
        return new AuthenticationResponse(jwt);
    }

    public void logout(String token) {
        jwtService.invalidateToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
    }
}