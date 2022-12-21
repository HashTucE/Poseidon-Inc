package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {


    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserService userService;

    @Mock
    private UserController userController;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private SecurityContext securityContext;

    @Mock
    SecurityContextLogoutHandler securityContextLogoutHandler;



    @Test
    public void urlTest() {

        //given
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/login");

        //when
        ModelAndView result = loginController.url();

        //then
        assertEquals(mav.getViewName(), result.getViewName());
        assertEquals("redirect:/login", result.getViewName());
    }


    @Test
    public void loginTest() {

        //given
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");

        //when
        ModelAndView result = loginController.login();

        //then
        assertEquals(mav.getViewName(), result.getViewName());
        assertEquals("login", result.getViewName());
    }


    @Test
    public void testGetAllUserArticles() {

        //given
        when(userService.findAll()).thenReturn(Arrays.asList(
                new User(0, "john", "a", "a", "USER"),
                new User(1, "jane", "a", "a", "USER")));

        //when
        ModelAndView mav = loginController.getAllUserArticles();

        //then
        assertEquals("user/list", mav.getViewName());
        List<User> users = (List<User>) mav.getModel().get("users");
        assertEquals(2, users.size());
        assertEquals("john", users.get(0).getUsername());
        assertEquals("jane", users.get(1).getUsername());
    }


    @Test
    public void errorTest() {

        //given
        ModelAndView mav = new ModelAndView();
        mav.setViewName("403");
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);

        //when
        ModelAndView result = loginController.error();

        //then
        assertEquals(mav.getViewName(), result.getViewName());
        assertEquals("403", result.getViewName());
        assertEquals(mav.getModel().get("errorMsg"), result.getModel().get("errorMsg"));
        assertEquals("You are not authorized for the requested data.", result.getModel().get("errorMsg"));
    }


    @Test
    public void logoutPageTest() {

        //given
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        //when
        String result = loginController.logoutPage(httpServletRequest, httpServletResponse);

        //then
        assertEquals("redirect:/login", result);
    }


    @Test
    public void logoutPageNegativeTest() {

        //when
        String result = loginController.logoutPage(httpServletRequest, httpServletResponse);

        //then
        assertEquals("redirect:/login", result);
    }




}
