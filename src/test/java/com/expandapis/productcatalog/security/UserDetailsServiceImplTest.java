package com.expandapis.productcatalog.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.entity.User;
import com.expandapis.productcatalog.repositories.UserRepository;
import com.expandapis.productcatalog.service.BaseServiceTest;

@SpringBootTest
public class UserDetailsServiceImplTest extends BaseServiceTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testLoadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        // Given
        User user = new User(1L, "test1", "test123", Role.ROLE_USER);
        userRepository.save(user);

        // When
        UserDetails result = userDetailsService.loadUserByUsername(user.getUsername());

        // Then
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole().toString(), result.getAuthorities().iterator().next().toString());
    }

    @Test
    public void testLoadUserByUsername_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {
        // Given
        // No user in the database

        // When
        // Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("user"));
    }
}
