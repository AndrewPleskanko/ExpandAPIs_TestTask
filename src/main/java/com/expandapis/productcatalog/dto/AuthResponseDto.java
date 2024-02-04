package com.expandapis.productcatalog.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthResponseDto {
    @NotEmpty(message = "Access token cannot be empty")
    private String accessToken;
    private String tokenType = "Bearer ";
    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}