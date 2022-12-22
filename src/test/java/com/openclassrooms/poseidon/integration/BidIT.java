package com.openclassrooms.poseidon.integration;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.BidListService;
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
public class BidIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListService bidListService;

    private int id = 0;



    @BeforeEach
    public void before() {

        BidList bidList = new BidList("IT", "IT", 10.0);
        bidListService.addBid(bidList);
        id = bidList.getId();
    }

    @AfterEach
    public void after() {

        try {
            bidListService.deleteById(id);}
        catch (NotExistingException e) {}
    }


    @Test
    public void homeTest() throws Exception {

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk());
    }


    @Test
    public void validateTest() throws Exception {

        mockMvc.perform(post("/bidList/validate")
                        .param("account", "IT")
                        .param("type", "IT")
                        .param("bidQuantity", String.valueOf(10.0)))
                .andExpect(redirectedUrl("/bidList/list"))
                .andExpect(status().isFound());
    }


    @Test
    public void showUpdateFormTest() throws Exception {

        mockMvc.perform(get("/bidList/update/{id}", id))
                .andExpect(status().isOk());
    }


    @Test
    public void updateBidListTest() throws Exception {

        mockMvc.perform(post("/bidList/update/{id}", id)
                        .param("account", "IT")
                        .param("type", "IT")
                        .param("bidQuantity", "20.0"))
                .andExpect(redirectedUrl("/bidList/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    @Test
    public void deleteBidListTest() throws Exception {

        mockMvc.perform(get("/bidList/delete/{id}", id))
                .andExpect(redirectedUrl("/bidList/list"))
                .andExpect(status().isFound())
                .andReturn();
    }
}