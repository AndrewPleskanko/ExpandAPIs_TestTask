package com.example.backend.services.interfaces;

import java.util.List;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;

public interface UserService {
    void saveUser(UserDto request);

    User get(Long id);

    List<User> getList();

    User findByUsername(String username);
}
