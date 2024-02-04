package com.expandapis.productcatalog.utils;

import java.util.ArrayList;
import java.util.List;

import com.expandapis.productcatalog.dto.UserDto;
import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.entity.User;

public class UserTestUtils {
    public static UserDto createUserDto(String username, String password, Role role) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setRole(role);
        return userDto;
    }

    public static User createUser(String username, String password, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    public static List<User> createUsers(int count, Role role) {
        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            User user = createUser("user" + i, "password" + i, role);
            userList.add(user);
        }
        return userList;
    }
}
