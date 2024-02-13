package com.example.backend.services.interfaces;

import com.example.backend.dto.AuthResponseDto;
import com.example.backend.dto.UserDto;

public interface AuthenticationService {
    AuthResponseDto authenticateUser(UserDto userDto);
}
