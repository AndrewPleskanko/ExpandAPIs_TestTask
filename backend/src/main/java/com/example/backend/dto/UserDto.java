package com.example.backend.dto;

import com.example.backend.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class UserDto {
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Size(min = 4, message = "Email must be at least 4 characters long")
    private String email;

    @NotEmpty(message = "Role cannot be empty")
    private Role role;
}

