package com.openclassrooms.poseidon.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RatingIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ObjectMapper objectMapper;

    private int id = 0;

    private Rating rating;



    @BeforeEach
    public void before() {

        rating = new Rating("IT", "IT", "IT", 1);
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


    ////////////////////////////////////////REST CONTROLLERS////////////////////////////////////////



    @Test
    public void testGetAllRating() throws Exception {

        Rating rating0 = new Rating("IT0", "IT0", "IT0", 1);
        ratingService.addRating(rating0);
        int id0 = rating0.getId();

        mockMvc.perform(get("/api/rating/all"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$..id", hasItems(rating.getId(), rating0.getId())))
                .andExpect(jsonPath("$..moodysRating", hasItems(rating.getMoodysRating(), rating0.getMoodysRating())))
                .andExpect(jsonPath("$..sandPRating", hasItems(rating.getSandPRating(), rating0.getSandPRating())))
                .andExpect(jsonPath("$..fitchRating", hasItems(rating.getFitchRating(), rating0.getFitchRating())))
                .andExpect(jsonPath("$..orderNumber", hasItems(rating.getOrderNumber(), rating0.getOrderNumber())));

        ratingService.deleteById(id0);
    }


    @Test
    public void getRatingAPITest() throws Exception {

        mockMvc.perform(get("/api/rating")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.moodysRating").value("IT"))
                .andExpect(jsonPath("$.sandPRating").value("IT"))
                .andExpect(jsonPath("$.fitchRating").value("IT"))
                .andExpect(jsonPath("$.orderNumber").value(1));


    }


    @Test
    public void testAddRating() throws Exception {

        String requestBody = objectMapper.writeValueAsString(rating);

        mockMvc.perform(post("/api/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("Rating created !"));

        //check if object is well created
        mockMvc.perform(get("/api/rating")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.moodysRating").value(rating.getMoodysRating()))
                .andExpect(jsonPath("$.sandPRating").value(rating.getSandPRating()))
                .andExpect(jsonPath("$.fitchRating").value(rating.getFitchRating()))
                .andExpect(jsonPath("$.orderNumber").value(rating.getOrderNumber()));

        //check answer with invalid object
        rating.setMoodysRating("");
        requestBody = objectMapper.writeValueAsString(rating);
        mockMvc.perform(post("/api/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("MoodysRating is mandatory"));
    }


    @Test
    public void updateRatingAPITest() throws Exception {

        Rating ratingToUpdate = new Rating("IT0", "IT0", "IT0", 1);

        mockMvc.perform(put("/api/rating")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string("Rating with id " + id + " updated !"));

        //check if object is well updated
        mockMvc.perform(get("/api/rating")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.moodysRating").value(ratingToUpdate.getMoodysRating()))
                .andExpect(jsonPath("$.sandPRating").value(ratingToUpdate.getSandPRating()))
                .andExpect(jsonPath("$.fitchRating").value(ratingToUpdate.getFitchRating()))
                .andExpect(jsonPath("$.orderNumber").value(ratingToUpdate.getOrderNumber()));

        //check answer with invalid object
        rating.setMoodysRating("");
        String requestBody = objectMapper.writeValueAsString(rating);
        mockMvc.perform(put("/api/rating")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("MoodysRating is mandatory"));
    }



    @Test
    public void deleteRatingAPITest() throws Exception {

        mockMvc.perform(delete("/api/rating")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string("Rating with id " + id + " deleted !"));

        //check if object is well deleted
        mockMvc.perform(get("/api/rating" + id))
                .andExpect(status().isNotFound());
    }
}
