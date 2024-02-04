package com.expandapis.productcatalog.services.interfaces;

import com.expandapis.productcatalog.dto.AuthResponseDto;
import com.expandapis.productcatalog.dto.UserDto;

public interface AuthenticationService {
    AuthResponseDto authenticateUser(UserDto userDto);
}
