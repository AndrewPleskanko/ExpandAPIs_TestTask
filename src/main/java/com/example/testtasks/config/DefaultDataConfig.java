package  com.example.testtasks.config;

import  com.example.testtasks.dto.UserDTO;
import  com.example.testtasks.services.interfaces.UserServiceImp;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultDataConfig {

    private UserServiceImp userService;

    @Autowired
    public DefaultDataConfig(UserServiceImp userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void createDefaultUsers() {
        UserDTO johnRequest = new UserDTO();
        johnRequest.setUsername("john");
        johnRequest.setPassword("123");
        userService.saveUser(johnRequest);

        UserDTO janeRequest = new UserDTO();
        janeRequest.setUsername("jane");
        janeRequest.setPassword("123");
        userService.saveUser(janeRequest);

        UserDTO adminRequest = new UserDTO();
        adminRequest.setUsername("admin");
        adminRequest.setPassword("admin");

        userService.saveUser(adminRequest);

        UserDTO moderRequest = new UserDTO();
        moderRequest.setUsername("moder");
        moderRequest.setPassword("moder");
        userService.saveUser(moderRequest);

        UserDTO umoderatorRequest = new UserDTO();
        umoderatorRequest.setUsername("umoderator");
        umoderatorRequest.setPassword("12345");
        userService.saveUser(umoderatorRequest);
    }
}



