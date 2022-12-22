package com.openclassrooms.poseidon.integration;

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
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ActiveProfiles("test")
//@TestPropertySource(properties = "spring.profiles.active=test")
//@Sql({"/doc/schema.sql", "/doc/data.sql"})
public class UserIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private int id = 0;



    @BeforeEach
    public void before() {

        User user = new User("IT", "Passw0rd@", "IT", "USER");
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
}