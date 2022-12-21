package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {



    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult result;

    @Mock
    private Model model;


    @Test
    public void homeTest() {

        //given
        List<User> expectedUsers = Arrays.asList(
                new User(1, "a", "a", "a", "USER"),
                new User(2, "b", "a", "a", "USER"));
        when(userService.findAll()).thenReturn(expectedUsers);

        //when
        String string = userController.home(model);

        //then
        assertEquals("user/list", string);
        verify(model).addAttribute("users", expectedUsers);
        verify(userService, times(1)).findAll();
    }


    @Test
    public void addUserTest() {

        //given
        User user = new User();

        //when
        String result = userController.addUser(user);

        //then
        assertEquals("user/add", result);
    }


    @Test
    public void validateTest() {

        //given
        User user = new User(1, "a", "a", "a", "USER");
        when(result.hasErrors()).thenReturn(false);

        //when
        String string = userController.validate(user, result);

        //then
        assertEquals("redirect:/user/list", string);
        verify(userService, times(1)).addUser(user);
    }


    @Test
    public void validateNegativeTest() {

        //given
        User user = new User(1, "a", "a", "a", "USER");
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = userController.validate(user, result);

        //then
        assertEquals("user/add", string);
        verify(userService, times(0)).addUser(user);
    }


    @Test
    public void showUpdateFormTest() throws NotExistingException {

        //given
        User user = new User(1, "a", "a", "a", "USER");
        when(userService.findById(1)).thenReturn(user);

        //when
        String result = userController.showUpdateForm(1, model);

        //then
        assertEquals("user/update", result);
        verify(model).addAttribute("user", user);
        assertEquals("", user.getPassword());
    }




    @Test
    public void updateUserTest() throws NotExistingException {

        //given
        User user = new User(1, "a", "a", "a", "USER");
        when(result.hasErrors()).thenReturn(false);

        //when
        String string = userController.updateUser(1, user, result);

        //then
        assertEquals("redirect:/user/list", string);
        assertNotEquals("password", user.getPassword());
    }


    @Test
    public void updateUserNegativeTest() throws NotExistingException {

        //given
        User user = new User(1, "a", "a", "a", "USER");
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = userController.updateUser(1, user, result);

        //then
        assertEquals("user/update", string);
    }


    @Test
    public void deleteUserTest() throws NotExistingException {

        //given//when
        String string = userController.deleteUser(1);

        //then
        assertEquals("redirect:/user/list", string);
        verify(userService, times(1)).deleteById(1);
    }
}
