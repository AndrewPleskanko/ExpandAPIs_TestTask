package com.expandapis.productcatalog.services.interfaces;

import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO request);

    User get(Long id);

    List<User> getList();

    User findByUsername(String username);
}
