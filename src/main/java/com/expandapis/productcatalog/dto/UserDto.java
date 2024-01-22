package com.expandapis.productcatalog.dto;

import com.expandapis.productcatalog.entity.Role;
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

    @NotEmpty(message = "Role cannot be empty")
    private Role role;
}

