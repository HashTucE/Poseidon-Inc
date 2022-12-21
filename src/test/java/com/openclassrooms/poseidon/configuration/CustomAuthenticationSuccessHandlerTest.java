package com.openclassrooms.poseidon.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationSuccessHandlerTest {


    @InjectMocks
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private Authentication authentication;


    @Test
    public void onAuthenticationSuccessAdminTest() throws IOException {

        //given
        Collection authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        when(authentication.getAuthorities()).thenReturn(authorities);

        //when
        customAuthenticationSuccessHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);

        //then
        verify(httpServletResponse, times(1)).sendRedirect("/user/list");
    }


    @Test
    public void onAuthenticationSuccessUserTest() throws IOException {

        //given
        Collection authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        when(authentication.getAuthorities()).thenReturn(authorities);

        //when
        customAuthenticationSuccessHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);

        //then
        verify(httpServletResponse, times(1)).sendRedirect("/bidList/list");
    }
}
