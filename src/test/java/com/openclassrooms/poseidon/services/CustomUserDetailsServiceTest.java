package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;




@ContextConfiguration(classes = {CustomUserDetailsService.class})
@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {


    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserService userService;


    @Test
    public void loadUserByUsernameNegativeTest2() {

        //when
        when(userService.findByUsername(anyString())).thenReturn(null);

        //then
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(anyString()));
    }


    @Test
    void testLoadUserByUsernameAsUser() throws UsernameNotFoundException {

        //given
        User user = new User(1, "username", "pass", "name", "USER");

        //when
        when(userService.findByUsername(any())).thenReturn(user);
        UserDetails result = customUserDetailsService.loadUserByUsername("username");

        //then
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.isEnabled());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isAccountNonExpired());
        assertEquals("username", result.getUsername());
        assertEquals("pass", result.getPassword());
        verify(userService, times(1)).findByUsername(any());
    }


    @Test
    void testLoadUserByUsernameAsAdmin() throws UsernameNotFoundException {

        //given
        User user = new User(1, "username", "pass", "name", "ADMIN");

        //when
        when(userService.findByUsername(any())).thenReturn(user);
        UserDetails result = customUserDetailsService.loadUserByUsername("username");

        //then
        assertEquals(1, result.getAuthorities().size());
        assertEquals("username", result.getUsername());
        assertEquals("pass", result.getPassword());
        verify(userService, times(1)).findByUsername(any());
    }


    @Test
    void testLoadUserByUsernameAsElse() throws UsernameNotFoundException {

        //given
        User user = new User(1, "username", "pass", "name", "A");

        //when
        when(userService.findByUsername(any())).thenReturn(user);
        customUserDetailsService.loadUserByUsername("username");

        //then
        verify(userService, times(1)).findByUsername(any());
    }



}
