package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void findById() {
        Long id = 1L;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User returnedUser = response.getBody();
        assertEquals("test", returnedUser.getUsername());
        assertEquals("testPassword", returnedUser.getPassword());
    }

    @Test
    public void findByUsername() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword("testPassword");
        user.setId(1L);

        when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(username);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User returnedUser = response.getBody();
        assertEquals("test", returnedUser.getUsername());
        assertEquals("testPassword", returnedUser.getPassword());
    }


    @Test
    public void findByUsernameFail() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        user.setPassword("testPassword");
        user.setId(1L);

        when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(username + 2);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void createUser() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void createUserFail() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("test");
        createUserRequest.setConfirmPassword("test");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }
}
