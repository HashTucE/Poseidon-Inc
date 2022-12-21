package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }


    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }


    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result) {

        if (!result.hasErrors()) {
            userService.addUser(user);
            return "redirect:/user/list";
        }
        return "user/add";
    }


    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {
        User user = userService.findById(id);
        user.setPassword("");
        model.addAttribute("user", user);

        return "user/update";
    }


    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            userService.updateUser(id, user);
            return "redirect:/user/list";
        }
        return "user/update";
    }


    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) throws NotExistingException {

        userService.deleteById(id);
        return "redirect:/user/list";
    }
}
