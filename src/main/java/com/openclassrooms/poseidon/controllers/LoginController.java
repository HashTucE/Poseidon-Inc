package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {


    private static final Logger log = LogManager.getLogger(LoginController.class);

    @Autowired
    private UserService userService;


    /**
     * redirect to login
     * @return ModelAndView
     */
    @GetMapping("/")
    public ModelAndView url() {

        ModelAndView mav = new ModelAndView();
        log.info("redirect login view");

        mav.setViewName("redirect:/login");
        return mav;
    }


    /**
     * display login page
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView login() {

        ModelAndView mav = new ModelAndView();
        log.info("returning login view");

        mav.setViewName("login");
        return mav;
    }


    /**
     * display error page when request is unauthorized
     * @return ModelAndView
     */
    @GetMapping("error")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        log.info("returning unauthorized 403 view");
        return mav;
    }


    /**
     * redirect to login after logout
     * @return ModelAndView
     */
    @PostMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        log.error("logout successfully");
        return "redirect:/login";
    }


}
