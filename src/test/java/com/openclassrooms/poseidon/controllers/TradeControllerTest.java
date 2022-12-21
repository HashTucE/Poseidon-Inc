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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeControllerTest {



    @InjectMocks
    private TradeController tradeController;

    @Mock
    private TradeService tradeService;

    @Mock
    BindingResult result;

    @Mock
    private Model model;


    @Test
    void homeTest() throws EmptyListException {

        //given
        List<Trade> trades = Arrays.asList(new Trade(), new Trade());
        when(tradeService.findAll()).thenReturn(trades);

        //when
        String result = tradeController.home(model);

        //then
        assertEquals("trade/list", result);
        verify(model).addAttribute("tradeList", trades);
    }


    @Test
    void homeNegativeTest() throws EmptyListException {

        //given
        String alertMessage = "There is no trade yet.";
        doThrow(new EmptyListException("trade")).when(tradeService).findAll();

        //when
        String result = tradeController.home(model);

        //then
        assertEquals("trade/list", result);
        verify(model).addAttribute("alertMessage", alertMessage);
    }


    @Test
    public void addTradeFormTest() {

        //given
        Trade trade = new Trade();

        //when
        String result = tradeController.addTradeForm(trade);

        //then
        assertEquals("trade/add", result);
    }


    @Test
    public void validateTest() {

        //given
        Trade trade = new Trade();
        when(result.hasErrors()).thenReturn(false);

        //when
        String string = tradeController.validate(trade, result);

        //then
        assertEquals("redirect:/trade/list", string);
        verify(tradeService).addTrade(trade);
    }


    @Test
    public void validateNegativeTest() {

        //given
        Trade trade = new Trade();
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = tradeController.validate(trade, result);

        //then
        assertEquals("trade/add", string);
    }


    @Test
    public void showUpdateFormTest() throws NotExistingException {

        //given
        int id = 123;
        Trade trade = new Trade();
        when(tradeService.findById(id)).thenReturn(trade);

        //when
        String string = tradeController.showUpdateForm(id, model);

        //then
        assertEquals("trade/update", string);
        verify(model).addAttribute("trade", trade);
    }


    @Test
    public void updateTradeTest() throws NotExistingException {

        //given
        Trade trade = new Trade("account", "type");
        trade.setId(1);

        //when
        String string = tradeController.updateTrade(1, trade, result);

        //then
        assertEquals("redirect:/trade/list", string);
        verify(tradeService, times(1)).updateTrade(1, trade);
    }


    @Test
    public void updateTradeNegativeTest() throws NotExistingException {

        //given
        Trade trade = new Trade("account", "type");
        trade.setId(1);
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = tradeController.updateTrade(1, trade, result);

        //then
        assertEquals("trade/update", string);
    }


    @Test
    public void deleteTradeTest() throws NotExistingException {

        //given//when
        String string = tradeController.deleteTrade(1);

        //then
        assertEquals("redirect:/trade/list", string);
        verify(tradeService, times(1)).deleteById(1);
    }
}
