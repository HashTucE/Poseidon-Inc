package com.openclassrooms.poseidon.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.TradeService;
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
public class TradeIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ObjectMapper objectMapper;

    private int id = 0;

    private Trade trade;



    @BeforeEach
    public void before() {
        trade = new Trade("IT", "IT", 10.0);
        tradeService.addTrade(trade);
        id = trade.getId();
    }

    @AfterEach
    public void after() {

        try {tradeService.deleteById(id);}
            catch (NotExistingException e) {}
    }


    @Test
    public void homeTest() throws Exception {

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk());
    }


    @Test
    public void validateTest() throws Exception {

        mockMvc.perform(post("/trade/validate")
                        .param("account", "IT")
                        .param("type", "IT")
                        .param("buyQuantity", String.valueOf(10.0)))
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(status().isFound());
    }


    @Test
    public void showUpdateFormTest() throws Exception {

        mockMvc.perform(get("/trade/update/{id}", id))
                .andExpect(status().isOk());
    }


    @Test
    public void updateTradeTest() throws Exception {

        mockMvc.perform(post("/trade/update/{id}", id)
                        .param("account", "IT")
                        .param("type", "IT")
                        .param("buyQuantity", "20.0"))
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    @Test
    public void deleteTradeTest() throws Exception {

        mockMvc.perform(get("/trade/delete/{id}", id))
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(status().isFound())
                .andReturn();
    }


    ////////////////////////////////////////REST CONTROLLERS////////////////////////////////////////



    @Test
    public void testGetAllTrade() throws Exception {

        Trade trade0 = new Trade("IT0", "IT0", 10.0);
        tradeService.addTrade(trade0);
        int id0 = trade0.getId();

        mockMvc.perform(get("/api/trade/all"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$..id", hasItems(trade.getId(), trade0.getId())))
                .andExpect(jsonPath("$..account", hasItems(trade.getAccount(), trade0.getAccount())))
                .andExpect(jsonPath("$..type", hasItems(trade.getType(), trade0.getType())))
                .andExpect(jsonPath("$..buyQuantity", hasItems(trade.getBuyQuantity(), trade0.getBuyQuantity())));

        tradeService.deleteById(id0);
        }


    @Test
    public void getTradeAPITest() throws Exception {

        mockMvc.perform(get("/api/trade")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.account").value("IT"))
                .andExpect(jsonPath("$.type").value("IT"))
                .andExpect(jsonPath("$.buyQuantity").value(10.0));


    }


    @Test
    public void testAddTrade() throws Exception {

        String requestBody = objectMapper.writeValueAsString(trade);

        mockMvc.perform(post("/api/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("Trade created !"));

        //check if object is well created
        mockMvc.perform(get("/api/trade")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.account").value(trade.getAccount()))
                .andExpect(jsonPath("$.type").value(trade.getType()))
                .andExpect(jsonPath("$.buyQuantity").value(trade.getBuyQuantity()));

        //check answer with invalid object
        trade.setAccount("");
        requestBody = objectMapper.writeValueAsString(trade);
        mockMvc.perform(post("/api/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account is mandatory"));
    }


    @Test
    public void updateTradeAPITest() throws Exception {

        Trade tradeToUpdate = new Trade("IT2", "IT2", 1.0);

        mockMvc.perform(put("/api/trade")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string("Trade with id " + id + " updated !"));

        //check if object is well updated
        mockMvc.perform(get("/api/trade")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.account").value(tradeToUpdate.getAccount()))
                .andExpect(jsonPath("$.type").value(tradeToUpdate.getType()))
                .andExpect(jsonPath("$.buyQuantity").value(tradeToUpdate.getBuyQuantity()));

        //check answer with invalid object
        trade.setAccount("");
        String requestBody = objectMapper.writeValueAsString(trade);
        mockMvc.perform(put("/api/trade")
                        .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account is mandatory"));
    }


    @Test
    public void deleteTradeAPITest() throws Exception {

        mockMvc.perform(delete("/api/trade")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string("Trade with id " + id + " deleted !"));

        //check if object is well deleted
        mockMvc.perform(get("/api/trade" + id))
                .andExpect(status().isNotFound());
    }
}
