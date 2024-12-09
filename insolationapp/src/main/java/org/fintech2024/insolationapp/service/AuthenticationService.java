package org.fintech2024.insolationapp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.fintech2024.insolationapp.model.Role;
import org.fintech2024.insolationapp.model.SignInRequest;
import org.fintech2024.insolationapp.model.SignUpRequest;
import org.fintech2024.insolationapp.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.fintech2024.insolationapp.model.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${security_config.jwtExpirationTime}")
    private int jwtExpirationMs;

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void signUp(SignUpRequest request, HttpServletResponse response) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.ROLE_USER)
                .build();

        userService.createUser(user);

        var jwt = jwtService.generateToken(user);
        setJwtCookie(response, jwt);
    }

    public void signIn(SignInRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        setJwtCookie(response, jwt);
    }

    public void updateUserPassword(User user, String password, String confirmPassword) {
        if (password != null && !password.isBlank()) {
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Пароли не совпадают");
            }
            user.setPassword(passwordEncoder.encode(password));
        }

        userService.save(user);
    }

    private void setJwtCookie(HttpServletResponse response, String jwt) {
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(jwtExpirationMs / 1000);
        response.addCookie(jwtCookie);
    }
}