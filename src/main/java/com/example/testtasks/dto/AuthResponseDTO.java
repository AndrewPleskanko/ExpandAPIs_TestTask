package com.example.testtasks.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthResponseDTO {
    @NotEmpty(message = "Access token cannot be empty")
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}