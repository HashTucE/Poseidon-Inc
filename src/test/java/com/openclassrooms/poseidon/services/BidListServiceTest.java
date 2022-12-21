package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.BidListRepository;
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
public class BidListServiceTest {


    @InjectMocks
    private BidListService bidListService;

    @Mock
    private BidListRepository bidListRepository;

    private final BidList bidList = new BidList("account", "type", 10.0);



    @Test
    void findAllTest() throws EmptyListException {

        //given
        List<BidList> bids = Arrays.asList(new BidList(), new BidList(), new BidList());
        when(bidListRepository.findAll()).thenReturn(bids);

        //when
        List<BidList> result = bidListService.findAll();

        //then
        assertEquals(bids, result);
    }


    @Test
    void findAllNegativeTest() {

        //given
        when(bidListRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        try {
            bidListService.findAll();
        } catch (EmptyListException e) {

            //then
            assertEquals("There is no bid yet.", e.getMessage());
        }
    }


    @Test
    public void existsByIdTest() throws NotExistingException {

        //given
        int tradeId = 1;
        when(bidListRepository.existsById(tradeId)).thenReturn(true);

        //when
        boolean exists = bidListService.existsById(tradeId);

        //then
        assertTrue(exists);
        verify(bidListRepository, times(1)).existsById(tradeId);
    }


    @Test
    public void existsByIdNegativeTest() {

        //given
        int id = 1;
        when(bidListRepository.existsById(id)).thenReturn(false);

        //when
        try {
            bidListService.existsById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("bid with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void findByIdTest() throws NotExistingException {


        //given
        int id = 1;
        when(bidListRepository.findById(id)).thenReturn(Optional.of(bidList));

        //when
        bidListService.findById(id);

        //then
        verify(bidListRepository, times(1)).findById(1);
        assertEquals(bidList, bidListService.findById(id));
    }


    @Test
    void findByIdNegativeTest() {


        //given
        int id = 1;
        when(bidListRepository.findById(id)).thenReturn(Optional.empty());

        //when
        try {
            bidListService.findById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("bid with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void addBidTest() {

        //given//when
        bidListService.addBid(bidList);

        //then
        verify(bidListRepository, times(1)).save(any());
    }


    @Test
    void updateBidTest() throws NotExistingException {

        //given
        BidList optionalBidList = new BidList("account", "type2", 10.0);

        //when
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));
        bidListService.updateBid(1,optionalBidList);

        //then
        verify(bidListRepository, times(1)).save(any());
    }


    @Test
    void updateBidListNegativeTest() {

        //given
        when(bidListRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            bidListService.updateBid(1, bidList);
        } catch (NotExistingException e) {
            //then
            assertEquals("bid with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void deleteByIdTest() throws NotExistingException {

        //given//when
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));
        bidListService.deleteById(1);

        //then
        verify(bidListRepository, times(1)).delete(any());
    }


    @Test
    void deleteByIdNegativeTest() {

        //given
        when(bidListRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            bidListService.deleteById(1);
        } catch (NotExistingException e) {
            //then
            assertEquals("bid with id number 1 does not exist !", e.getMessage());
        }
    }
}
