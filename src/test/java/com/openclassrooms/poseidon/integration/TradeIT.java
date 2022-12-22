package com.openclassrooms.poseidon.integration;

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
public class TradeIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeService tradeService;

    private int id = 0;



    @BeforeEach
    public void before() {

        Trade trade = new Trade("IT", "IT", 10.0);
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
}
