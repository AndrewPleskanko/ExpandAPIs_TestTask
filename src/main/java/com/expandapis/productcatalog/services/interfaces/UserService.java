package com.expandapis.productcatalog.services.interfaces;

import com.expandapis.productcatalog.dto.UserDto;
import com.expandapis.productcatalog.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto request);

    User get(Long id);

    List<User> getList();

    User findByUsername(String username);
}
