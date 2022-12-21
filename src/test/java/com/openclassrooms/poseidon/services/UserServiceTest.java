package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    private final User user = new User(100,"a", "a", "a", "USER");


    @Test
    public void findAllTest() {

        //given
        List<User> expectedUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(expectedUsers);

        //when
        List<User> actualUsers = userService.findAll();

        //then
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findAll();
    }


    @Test
    public void existsByIdTest() throws NotExistingException {

        //given
        int id = 1;
        when(userRepository.existsById(id)).thenReturn(true);

        //when
        boolean exists = userService.existsById(id);

        //then
        assertTrue(exists);
        verify(userRepository, times(1)).existsById(id);
    }


    @Test
    public void existsByIdNegativeTest() {

        //given
        int id = 1;
        when(userRepository.existsById(id)).thenReturn(false);

        //when
        try {
            userService.existsById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("user with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    public void findByIdTest() throws NotExistingException {

        //given
        int id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        userService.findById(id);

        //then
        verify(userRepository, times(1)).findById(1);
        assertEquals(user, userService.findById(id));
    }


    @Test
    void findByIdNegativeTest() {


        //given
        int id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //when
        try {
            userService.findById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("user with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    public void addUserTest() {

        //given
        User expectedUser = new User(1, "a", "a", "a", "USER");
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        //when
        User actualUser = userService.addUser(expectedUser);

        //then
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).save(expectedUser);
    }


    @Test
    void updateUserTest() throws NotExistingException {

        //given
        User optionalUser = new User(1, "a", "a", "a", "USER");

        //when
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        userService.updateUser(1,optionalUser);

        //then
        verify(userRepository, times(1)).save(any());
    }


    @Test
    void updateUserNegativeTest() {

        //given
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            userService.updateUser(1, user);
        } catch (NotExistingException e) {
            //then
            assertEquals("user with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void deleteByIdTest() throws NotExistingException {

        //given//when
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        userService.deleteById(1);

        //then
        verify(userRepository, times(1)).delete(any(User.class));
    }


    @Test
    void deleteByIdNegativeTest() {

        //given
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            userService.deleteById(1);
        } catch (NotExistingException e) {
            //then
            assertEquals("user with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    public void findByUsernameTest() {

        //given
        String expectedUsername = "test";
        User expectedUser = new User(1, "test", "a", "a", "USER");
        when(userRepository.findByUsername(expectedUsername)).thenReturn(expectedUser);

        //when
        User actualUser = userService.findByUsername(expectedUsername);

        //then
        assertEquals(expectedUser, actualUser);
        verify(userRepository).findByUsername(expectedUsername);
    }

}
