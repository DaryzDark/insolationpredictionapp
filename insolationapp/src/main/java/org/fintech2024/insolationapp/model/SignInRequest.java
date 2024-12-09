package org.fintech2024.insolationapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SignInRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}