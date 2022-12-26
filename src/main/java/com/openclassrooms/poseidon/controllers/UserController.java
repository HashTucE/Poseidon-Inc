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


    /**
     * display the user list
     * @param model model
     * @return string path
     */
    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }


    /**
     * display add user form
     * @return string path
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }


    /**
     * redirect after add user
     * @param user user
     * @param result result
     * @return string path
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result) {

        if (!result.hasErrors()) {
            userService.addUser(user);
            return "redirect:/user/list";
        }
        return "user/add";
    }


    /**
     * display update user form
     * @param id id
     * @param model model
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {
        User user = userService.findById(id);
        user.setPassword("");
        model.addAttribute("user", user);

        return "user/update";
    }


    /**
     * redirect after update user
     * @param id id
     * @param user user
     * @param result result
     * @return string path
     * @throws NotExistingException e
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            userService.updateUser(id, user);
            return "redirect:/user/list";
        }
        return "user/update";
    }


    /**
     * redirect after delete user
     * @param id id
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) throws NotExistingException {

        userService.deleteById(id);
        return "redirect:/user/list";
    }
}
