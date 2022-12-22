package com.openclassrooms.poseidon.integration;

import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RatingService;
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
public class RatingIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingService ratingService;

    private int id = 0;



    @BeforeEach
    public void before() {

        Rating rating = new Rating("IT", "IT", "IT", 1);
        ratingService.addRating(rating);
        id = rating.getId();
    }

    @AfterEach
    public void after() {

        try {
            ratingService.deleteById(id);}
        catch (NotExistingException e) {}
    }


    @Test
    public void homeTest() throws Exception {

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk());
    }


    @Test
    public void validateTest() throws Exception {

        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "IT")
                        .param("sandPRating", "IT")
                        .param("fitchRating", "IT")
                        .param("orderNumber", String.valueOf(2)))
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(status().isFound());
    }


    @Test
    public void showUpdateFormTest() throws Exception {

        mockMvc.perform(get("/rating/update/{id}", id))
                .andExpect(status().isOk());
    }


    @Test
    public void updateRatingTest() throws Exception {

        mockMvc.perform(post("/rating/update/{id}", id)
                        .param("moodysRating", "IT")
                        .param("sandPRating", "IT")
                        .param("fitchRating", "IT")
                        .param("orderNumber", String.valueOf(2)))
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    @Test
    public void deleteRatingTest() throws Exception {

        mockMvc.perform(get("/rating/delete/{id}", id))
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(status().isFound())
                .andReturn();
    }
}
