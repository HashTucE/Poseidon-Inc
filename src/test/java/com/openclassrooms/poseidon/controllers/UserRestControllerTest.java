package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {



    @InjectMocks
    private UserRestController userRestController;

    @Mock
    private UserService userService;



    @Test
    public void getAllUserTest() {

        //given
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(1,"user", "pass", "name", "USER"));
        expectedUsers.add(new User(2,"user", "pass", "name", "USER"));
        when(userService.findAll()).thenReturn(expectedUsers);

        //when
        ResponseEntity<List<User>> response = userRestController.getAllUsers();

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
    }


    @Test
    public void getAllUserNegativeTest() {

        //given
        when(userService.findAll()).thenReturn(new ArrayList<>());

        //when
        ResponseEntity<List<User>> response = userRestController.getAllUsers();

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void getUserTest() throws NotExistingException {

        //given
        User expectedUser = new User(1,"user", "pass", "name", "USER");
        when(userService.findById(1)).thenReturn(expectedUser);

        //when
        ResponseEntity<User> response = userRestController.getUser(1);

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }


    @Test
    public void getTradeNegativeTest() throws NotExistingException {

        //given
        when(userService.findById(1)).thenReturn(null);

        //when
        ResponseEntity<User> response = userRestController.getUser(1);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void AddUserNegativeTest() {

        //given
        User user = new User(1, "user", "Passw0rd&", "", "USER");


        //when
        ResponseEntity<String> response = userRestController.addUser(user);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Fullname is mandatory", response.getBody());
    }

    @Test
    public void AddUserNegativeTest2() {

        //given
        User user = new User(1, "", "Passw0rd&", "fullname", "USER");

        //when
        ResponseEntity<String> response = userRestController.addUser(user);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username is mandatory", response.getBody());
    }

    @Test
    public void AddUserNegativeTest3() {

        //given
        User user = new User(1, "user", "password", "fullname", "USER");

        //when
        ResponseEntity<String> response = userRestController.addUser(user);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password must contain at least 8 characters, a capital letter, a digit and a symbol", response.getBody());
    }

    @Test
    public void AddUserNegativeTest4() {

        //given
        User user = new User(1, "user", "Passw0rd@", "fullname", "");

        //when
        ResponseEntity<String> response = userRestController.addUser(user);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Role 'USER' or 'ADMIN' is mandatory", response.getBody());
    }

    @Test
    public void AddUserTest() {

        //given
        User user = new User(1, "user", "Passw0rd@", "fullname", "USER");

        //when
        ResponseEntity<String> response = userRestController.addUser(user);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User with id 1 created !", response.getBody());
    }


    @Test
    public void updateUserTest() throws NotExistingException {

        //given
        User user = new User(1,"user", "Passw0rd&", "name", "USER");
        when(userService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = userRestController.updateUser(1, user);

        //then
        verify(userService).updateUser(1, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void updateUserNegativeTest() throws NotExistingException {

        //given
        User user = new User(1,"user", "pass", "name", "USER");
        when(userService.existsById(1)).thenReturn(false);

        //when
        ResponseEntity<String> response = userRestController.updateUser(1, user);

        //then
        verify(userService, never()).updateUser(1, user);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void updateUserNegativeTest2() throws NotExistingException {

        //given
        User user = new User(1, "user", "Passw0rd@", "", "USER");
        when(userService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = userRestController.updateUser(1, user);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Fullname is mandatory", response.getBody());
    }


    @Test
    public void updateUserNegativeTest3() throws NotExistingException {

        //given
        User user = new User(1, "", "Passw0rd@", "fullname", "USER");
        when(userService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = userRestController.updateUser(1, user);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username is mandatory", response.getBody());
    }


    @Test
    public void updateUserNegativeTest4() throws NotExistingException {

        //given
        User user = new User(1, "user", "Pass", "fullname", "USER");
        when(userService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = userRestController.updateUser(1, user);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password must contain at least 8 characters, a capital letter, a digit and a symbol", response.getBody());
    }


    @Test
    public void updateUserNegativeTest5() throws NotExistingException {

        //given
        User user = new User(1, "user", "Passw0rd@", "fullname", "");
        when(userService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = userRestController.updateUser(1, user);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Role 'USER' or 'ADMIN' is mandatory", response.getBody());
    }


    @Test
    public void deleteUserTest() throws NotExistingException {

        // given
        int id = 1;
        User user = new User(1,"user", "pass", "name", "USER");
        user.setId(id);
        when(userService.existsById(id)).thenReturn(true);

        // when
        ResponseEntity<String> response = userRestController.deleteUser(id);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).existsById(id);
        verify(userService).deleteById(id);
    }


    @Test
    public void deleteUserNegativeTest() throws NotExistingException {

        // given
        int id = 1;
        when(userService.existsById(id)).thenReturn(false);

        // when
        ResponseEntity<String> response = userRestController.deleteUser(id);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).existsById(id);
    }
}
