package com.expandapis.productcatalog.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}

