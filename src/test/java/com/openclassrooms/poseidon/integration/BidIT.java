package com.openclassrooms.poseidon.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class BidIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListService bidListService;

    @Autowired
    private ObjectMapper objectMapper;

    private int id = 0;

    private BidList bidList;


    @BeforeEach
    public void before() {

        bidList = new BidList("IT", "IT", 10.0);
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


    ////////////////////////////////////////REST CONTROLLERS////////////////////////////////////////



    @Test
    public void testGetAllBids() throws Exception {

        BidList bidList0 = new BidList("IT0", "IT0", 10.0);
        bidListService.addBid(bidList0);
        int id0 = bidList0.getId();

        mockMvc.perform(get("/api/bidList/all"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$..id", hasItems(bidList.getId(), bidList0.getId())))
                .andExpect(jsonPath("$..account", hasItems(bidList.getAccount(), bidList0.getAccount())))
                .andExpect(jsonPath("$..type", hasItems(bidList.getType(), bidList0.getType())))
                .andExpect(jsonPath("$..bidQuantity", hasItems(bidList.getBidQuantity(), bidList0.getBidQuantity())));

        bidListService.deleteById(id0);
    }


    @Test
    public void getBidAPITest() throws Exception {

        mockMvc.perform(get("/api/bidList")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.account").value("IT"))
                .andExpect(jsonPath("$.type").value("IT"))
                .andExpect(jsonPath("$.bidQuantity").value(10.0));


    }


    @Test
    public void testAddBid() throws Exception {

        String requestBody = objectMapper.writeValueAsString(bidList);

        mockMvc.perform(post("/api/bidList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("Bid with id " + id + " created !"));

        //check if object is well created
        mockMvc.perform(get("/api/bidList")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.account").value(bidList.getAccount()))
                .andExpect(jsonPath("$.type").value(bidList.getType()))
                .andExpect(jsonPath("$.bidQuantity").value(bidList.getBidQuantity()));

        //check answer with invalid object
        bidList.setAccount("");
        requestBody = objectMapper.writeValueAsString(bidList);
        mockMvc.perform(post("/api/bidList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account is mandatory"));
    }


    @Test
    public void updateBidAPITest() throws Exception {

        BidList bidToUpdate = new BidList("IT2", "IT2", 1.0);

        mockMvc.perform(put("/api/bidList")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bidToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bid with id " + id + " updated !"));

        //check if object is well updated
        mockMvc.perform(get("/api/bidList")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.account").value(bidToUpdate.getAccount()))
                .andExpect(jsonPath("$.type").value(bidToUpdate.getType()))
                .andExpect(jsonPath("$.bidQuantity").value(bidToUpdate.getBidQuantity()));

        //check answer with invalid object
        bidList.setAccount("");
        String requestBody = objectMapper.writeValueAsString(bidList);
        mockMvc.perform(put("/api/bidList")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account is mandatory"));
    }


    @Test
    public void deleteBidAPITest() throws Exception {

        mockMvc.perform(delete("/api/bidList")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bid with id " + id + " deleted !"));

        //check if object is well deleted
        mockMvc.perform(get("/bidList/" + id))
                .andExpect(status().isNotFound());
    }
}
