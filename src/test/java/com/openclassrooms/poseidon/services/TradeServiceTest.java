package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {


    @InjectMocks
    private TradeService tradeService;

    @Mock
    private TradeRepository tradeRepository;

    private final Trade trade = new Trade("account", "type");



    @Test
    void findAllTest() throws EmptyListException {

        //given
        List<Trade> trades = Arrays.asList(new Trade(), new Trade(), new Trade());
        when(tradeRepository.findAll()).thenReturn(trades);

        //when
        List<Trade> result = tradeService.findAll();

        //then
        assertEquals(trades, result);
    }


    @Test
    void findAllNegativeTest() {

        //given
        when(tradeRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        try {
            tradeService.findAll();
        } catch (EmptyListException e) {

            //then
            assertEquals("There is no trade yet.", e.getMessage());
        }
    }


    @Test
    public void existsByIdTest() throws NotExistingException {

        //given
        int tradeId = 1;
        when(tradeRepository.existsById(tradeId)).thenReturn(true);

        //when
        boolean exists = tradeService.existsById(tradeId);

        //then
        assertTrue(exists);
        verify(tradeRepository, times(1)).existsById(tradeId);
    }


    @Test
    public void existsByIdNegativeTest() {

        //given
        int id = 1;
        when(tradeRepository.existsById(id)).thenReturn(false);

        //when
        try {
            tradeService.existsById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("trade with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void findByIdTest() throws NotExistingException {


        //given
        int id = 1;
        when(tradeRepository.findById(id)).thenReturn(Optional.of(trade));

        // when
        tradeService.findById(id);

        //then
        verify(tradeRepository, times(1)).findById(1);
        assertEquals(trade, tradeService.findById(id));
    }


    @Test
    void findByIdNegativeTest() {


        //given
        int id = 1;
        when(tradeRepository.findById(id)).thenReturn(Optional.empty());

        //when
        try {
            tradeService.findById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("trade with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void addTradeTest() {

        //given//when
        tradeService.addTrade(trade);

        //then
        verify(tradeRepository, times(1)).save(any());
    }


    @Test
    void updateTradeTest() throws NotExistingException {

        //given
        Trade optionalTrade = new Trade("account", "type2");

        //when
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));
        tradeService.updateTrade(1,optionalTrade);

        //then
        verify(tradeRepository, times(1)).save(any());
    }


    @Test
    void updateTradeNegativeTest() {

        //given
        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            tradeService.updateTrade(1, trade);
        } catch (NotExistingException e) {
            //then
            assertEquals("trade with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void deleteByIdTest() throws NotExistingException {

        //given//when
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));
        tradeService.deleteById(1);

        //then
        verify(tradeRepository, times(1)).delete(any());
    }


    @Test
    void deleteByIdNegativeTest() {

        //given
        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            tradeService.deleteById(1);
        } catch (NotExistingException e) {
            //then
            assertEquals("trade with id number 1 does not exist !", e.getMessage());
        }
    }
}
