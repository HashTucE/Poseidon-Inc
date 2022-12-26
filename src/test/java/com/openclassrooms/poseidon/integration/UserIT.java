package com.openclassrooms.poseidon.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private int id = 0;

    private User user;



    @BeforeEach
    public void before() {

        user = new User("IT", "Passw0rd@", "IT", "USER");
        userService.addUser(user);
        id = user.getId();
    }

    @AfterEach
    public void after() {

        try {
            userService.deleteById(id);}
        catch (NotExistingException e) {}
    }


    @Test
    public void homeTest() throws Exception {

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk());
    }


    @Test
    public void validateTest() throws Exception {

        mockMvc.perform(post("/user/validate")
                        .param("username", "IT")
                        .param("password", "Passw0rd@")
                        .param("fullname", "IT")
                        .param("role", "USER"))
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(status().isFound());
    }


    @Test
    public void showUpdateFormTest() throws Exception {

        mockMvc.perform(get("/user/update/{id}", id))
                .andExpect(status().isOk());
    }


    @Test
    public void updateUserTest() throws Exception {

        mockMvc.perform(post("/user/update/{id}", id)
                        .param("username", "IT")
                        .param("password", "Passw0rd@")
                        .param("fullname", "IT")
                        .param("role", "ADMIN"))
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    @Test
    public void deleteUserTest() throws Exception {

        mockMvc.perform(get("/user/delete/{id}", id))
                .andExpect(redirectedUrl("/user/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    ////////////////////////////////////////REST CONTROLLERS////////////////////////////////////////



    @Test
    public void testGetAllUser() throws Exception {

        User user0 = new User("IT", "Passw0rd@", "IT", "USER");
        userService.addUser(user0);
        int id0 = user0.getId();

        mockMvc.perform(get("/api/user/all"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$..id", hasItems(user.getId(), user0.getId())))
                .andExpect(jsonPath("$..username", hasItems(user.getUsername(), user0.getUsername())))
                .andExpect(jsonPath("$..password", hasItems(user.getPassword(), user0.getPassword())))
                .andExpect(jsonPath("$..fullname", hasItems(user.getFullname(), user0.getFullname())))
                .andExpect(jsonPath("$..role", hasItems(user.getRole(), user0.getRole())));

        userService.deleteById(id0);
    }


    @Test
    public void getUserAPITest() throws Exception {

        mockMvc.perform(get("/api/user")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.fullname").value(user.getFullname()))
                .andExpect(jsonPath("$.role").value(user.getRole()));


    }


    @Test
    public void addUserAPITest() throws Exception {

        String requestBody = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("User with id " + id + " created !"));

        //check if object is well created
        mockMvc.perform(get("/api/user")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.fullname").value(user.getFullname()))
                .andExpect(jsonPath("$.role").value(user.getRole()));

        //check answer with invalid object
        user.setUsername("");
        requestBody = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is mandatory"));
    }


    @Test
    public void updateUserAPITest() throws Exception {

        User userToUpdate = new User("IT2", "Passw0rd@", "IT2", "USER");

        mockMvc.perform(put("/api/user")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id " + id + " updated !"));

        //check if object is well updated
        mockMvc.perform(get("/api/user")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.username").value(userToUpdate.getUsername()))
                .andExpect(jsonPath("$.fullname").value(userToUpdate.getFullname()))
                .andExpect(jsonPath("$.role").value(userToUpdate.getRole()));

        //check answer with invalid object
        user.setUsername("");
        String requestBody = objectMapper.writeValueAsString(user);
        mockMvc.perform(put("/api/user")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is mandatory"));
    }



    @Test
    public void deleteUserAPITest() throws Exception {

        mockMvc.perform(delete("/api/user")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id " + id + " deleted !"));

        //check if object is well deleted
        mockMvc.perform(get("/api/user" + id))
                .andExpect(status().isNotFound());
    }
}