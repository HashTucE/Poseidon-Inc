package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.configuration.ValidPassword;
import com.openclassrooms.poseidon.constants.Log;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.UserService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path = "/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestController {



    private static final Logger log = LogManager.getLogger(UserRestController.class);


    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";


    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    //GET, POST, PUT, DELETE CONTROLLERS//



    /**
     * Find all users
     * @return users with HTTP code 200 OK
     */
    @GetMapping("/user/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(users, HttpStatus.FOUND);
        }
    }


    /**
     * Find a user by id
     * @param id of the user
     * @return user with HTTP code 200 found
     */
    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam int id) throws NotExistingException {
        User user = userService.findById(id);
        if (user != null) {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Create a user
     * @param user user
     * @return user with HTTP code 201 created
     */
    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(user.getPassword());

        if (user.getFullname().isBlank()) {
            return new ResponseEntity<>("Fullname is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (user.getUsername().isBlank()) {
            return new ResponseEntity<>("Username is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (!matcher.matches()) {
            log.error("Invalid password");
            return new ResponseEntity<>("Password must contain at least 8 characters, a capital letter, a digit and a symbol", HttpStatus.BAD_REQUEST);
        }
        if (user.getRole().isBlank()) {
            return new ResponseEntity<>("Role 'USER' or 'ADMIN' is mandatory", HttpStatus.BAD_REQUEST);
        }
        userService.addUser(user);
        log.info(Log.OBJECT_CREATED);
        return new ResponseEntity<>("User created !", HttpStatus.CREATED);
    }


    /**
     * Update a user
     * @param id of the user and user object
     * @return HTTP code 200 OK with confirmation string
     */
    @PutMapping("/user")
    public ResponseEntity<String> updateUser(@RequestParam int id, @RequestBody User user) throws NotExistingException {

        if(userService.existsById(id)) {
            userService.updateUser(id, user);
            log.info(Log.OBJECT_MODIFIED);
            return ResponseEntity.ok().body("User with id " + id + " updated !");
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete a user
     * @param id of the user
     * @return HTTP code 200 OK with confirmation string
     */
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int id) throws NotExistingException {

        if(userService.existsById(id)) {
            userService.deleteById(id);
            log.info(Log.OBJECT_DELETED);
            return ResponseEntity.ok().body("User with id " + id + " deleted !");
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
