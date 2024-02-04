package com.expandapis.productcatalog.services.interfaces;

import java.util.List;

import com.expandapis.productcatalog.dto.UserDto;
import com.expandapis.productcatalog.entity.User;

public interface UserService {
    void saveUser(UserDto request);

    User get(Long id);

    List<User> getList();

    User findByUsername(String username);
}
