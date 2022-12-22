package com.openclassrooms.poseidon.integration;

import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.repositories.TradeRepository;
import com.openclassrooms.poseidon.services.TradeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Sql({"/doc/schema.sql", "/doc/data.sql"})
public class TradeIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeRepository tradeRepository;




//    @Test
//    public void testHome_withTrades_shouldReturnTradeListPage() throws Exception {
//        // Given
//        Trade trade1 = new Trade("a", "a", 1.0);
//
//        Trade trade2 = new Trade("a", "a", 1.0);
//        tradeRepository.saveAll(Arrays.asList(trade1, trade2));
//
//        // When
//        MvcResult result = mockMvc.perform(get("/trade/list"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("trade/list"))
//                .andReturn();
//
//        // Then
//        List<Trade> trades = (List<Trade>) result.getModelAndView().getModel().get("tradeList");
//        assertEquals(2, trades.size());
//        assertEquals(1, trades.get(0).getId());
////        assertEquals(2, trades.get(1).g
//    }
//    @Test
//    public void testHome() throws Exception {
//        mockMvc.perform(get("/trade/list"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("trade/list"))
//                .andExpect(model().attributeExists("tradeList"));
//    }
//
//
//    @Test
//    public void testAddTradeForm() throws Exception {
//        mockMvc.perform(get("/trade/add"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("trade/add"))
//                .andExpect(model().attributeExists("trade"))
//                .andExpect(model().attribute("trade", instanceOf(Trade.class)));
//    }
//
//
//    @Test
//    public void testValidate_Success() throws Exception {
//
//        Trade trade = new Trade("account", "type", 10.0);
//
//        mockMvc.perform(post("/trade/validate")
//                        .param("account", trade.getAccount())
//                        .param("type", trade.getType())
//                        .param("buyQuantity", String.valueOf(10.0)))
//                .andExpect(model().attribute("tradeList", tradeService.findAll()))
////                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/trade/list"));
//    }
//
//
//    @Test
//    public void testShowUpdateForm_Success() throws Exception {
//        Trade trade = new Trade("account", "type", 10.0);
//        tradeService.addTrade(trade);
//
//        mockMvc.perform(get("/trade/update/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("trade/update"))
//                .andExpect(model().attributeExists("trade"))
//                .andExpect(model().attribute("trade", instanceOf(Trade.class)));
//    }
////
////    @Test
////    public void testShowUpdateForm_NotFound() throws Exception {
////        mockMvc.perform(get("/trade/update/1"))
////                .andExpect(status().isNotFound())
////                .andExpect(view().name("error/404"))
////                .andExpect(model().attributeDoesNotExist("trade"));
////    }
////
////
//    @Test
//    public void testDeleteTrade() throws Exception {
//        // Arrange
//        Integer id = 1;
//        Trade trade = new Trade("account", "type", 10.0);
//        trade.setId(id);
//        tradeService.addTrade(trade);
//
//        // Act
//        mockMvc.perform(get("/trade/delete/{id}", id))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/trade/list"))
//                .andExpect(model().attributeExists("tradeList"))
//                .andExpect(model().attribute("tradeList", Matchers.hasSize(1)))
//                .andExpect(model().attribute("tradeList", Matchers.hasItem(trade)));
//
//        // Assert
//        List<Trade> tradeList = tradeService.findAll();
//        assertEquals(0, tradeList.size());
//    }
//
//
//    @Test
//    public void testValidate() throws Exception {
//
//
//        Trade trade = new Trade("account", "type", 10.0);
//
//        List<Trade> actualTradeList = new ArrayList<>();
//        actualTradeList.add(trade);
//
////.with(user("admin").password("pass").roles("USER","ADMIN")
//        mockMvc.perform(post("/trade/validate")
//                        .param("account", trade.getAccount())
//                        .param("type", trade.getType())
//                        .param("buyQuantity", String.valueOf(trade.getBuyQuantity())))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/trade/list"));
//
//
//        List<Trade> expectedTradeList = tradeService.findAll();
//        assertSame(expectedTradeList, actualTradeList);
//
//
//
//
//
//    }




}
