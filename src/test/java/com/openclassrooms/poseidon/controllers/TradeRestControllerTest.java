package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.TradeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeRestControllerTest {



    @InjectMocks
    private TradeRestController tradeRestController;

    @Mock
    private TradeService tradeService;



    @Test
    public void getAllTradeTest() throws EmptyListException {

        //given
        List<Trade> expectedTrades = new ArrayList<>();
        expectedTrades.add(new Trade("account", "type"));
        expectedTrades.add(new Trade("account", "type"));
        when(tradeService.findAll()).thenReturn(expectedTrades);

        //when
        ResponseEntity<List<Trade>> response = tradeRestController.getAllTrade();

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedTrades, response.getBody());
    }


    @Test
    public void getAllTradeNegativeTest() throws EmptyListException {

        //given
        when(tradeService.findAll()).thenReturn(new ArrayList<>());

        //when
        ResponseEntity<List<Trade>> response = tradeRestController.getAllTrade();

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void getTradeTest() throws NotExistingException {

        //given
        Trade expectedTrade = new Trade("account", "type");
        when(tradeService.findById(1)).thenReturn(expectedTrade);

        //when
        ResponseEntity<Trade> response = tradeRestController.getTrade(1);

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedTrade, response.getBody());
    }


    @Test
    public void getTradeNegativeTest() throws NotExistingException {

        //given
        when(tradeService.findById(1)).thenReturn(null);

        //when
        ResponseEntity<Trade> response = tradeRestController.getTrade(1);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void AddTradeNegativeTest() {

        //given
        Trade trade = new Trade();
        trade.setAccount("");
        trade.setType("type");
        trade.setBuyQuantity(1.0);

        //when
        ResponseEntity<String> response = tradeRestController.addTrade(trade);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Account is mandatory", response.getBody());
    }


    @Test
    public void AddTradeNegativeTest2() {

        //given
        Trade trade = new Trade();
        trade.setAccount("account");
        trade.setType("");
        trade.setBuyQuantity(1.0);

        //when
        ResponseEntity<String> response = tradeRestController.addTrade(trade);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Type is mandatory", response.getBody());
    }


    @Test
    public void AddTradeNegativeTest3() {

        //given
        Trade trade = new Trade();
        trade.setAccount("account");
        trade.setType("type");
        trade.setBuyQuantity(null);

        //when
        ResponseEntity<String> response = tradeRestController.addTrade(trade);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("BuyQuantity is mandatory", response.getBody());
    }


    @Test
    public void AddTradeTest() {

        //given
        Trade trade = new Trade();
        trade.setAccount("account");
        trade.setType("type");
        trade.setBuyQuantity(1.0);

        //when
        ResponseEntity<String> response = tradeRestController.addTrade(trade);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Trade created !", response.getBody());
    }


    @Test
    public void updateTradeTest() throws NotExistingException {

        //given
        Trade trade = new Trade("account", "type");
        when(tradeService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = tradeRestController.updateTrade(1, trade);

        //then
        verify(tradeService).updateTrade(1, trade);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void updateTradeNegativeTest() throws NotExistingException {

        //given
        Trade trade = new Trade("account", "type");
        when(tradeService.existsById(1)).thenReturn(false);

        //when
        ResponseEntity<String> response = tradeRestController.updateTrade(1, trade);

        //then
        verify(tradeService, never()).updateTrade(1, trade);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void deleteTradeTest() throws NotExistingException {

        // given
        int tradeId = 1;
        Trade trade = new Trade();
        trade.setId(tradeId);
        when(tradeService.existsById(tradeId)).thenReturn(true);

        // when
        ResponseEntity<String> response = tradeRestController.deleteTrade(tradeId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tradeService).existsById(tradeId);
        verify(tradeService).deleteById(tradeId);
    }


    @Test
    public void deleteTradeNegativeTest() throws NotExistingException {

        // given
        int tradeId = 1;
        when(tradeService.existsById(tradeId)).thenReturn(false);

        // when
        ResponseEntity<String> response = tradeRestController.deleteTrade(tradeId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(tradeService).existsById(tradeId);
    }
}