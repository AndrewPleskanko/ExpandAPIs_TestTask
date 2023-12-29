package  com.example.testtasks.services.interfaces;

import com.example.testtasks.dto.UserDTO;
import com.example.testtasks.entity.User;

public interface UserService {
    void saveUser(UserDTO request);
    User get(Long id);
}
