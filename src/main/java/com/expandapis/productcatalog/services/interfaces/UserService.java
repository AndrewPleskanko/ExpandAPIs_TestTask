package com.expandapis.productcatalog.services.interfaces;

import com.expandapis.productcatalog.dto.UserDTO;
import com.expandapis.productcatalog.entity.User;

public interface UserService {
    void saveUser(UserDTO request);

    User get(Long id);
}
