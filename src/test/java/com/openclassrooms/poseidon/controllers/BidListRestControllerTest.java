package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.BidListService;
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
public class BidListRestControllerTest {



    @InjectMocks
    private BidListRestController bidListRestController;

    @Mock
    private BidListService bidListService;



    @Test
    public void getAllBidTest() throws EmptyListException {

        //given
        List<BidList> expectedBids = new ArrayList<>();
        expectedBids.add(new BidList("account", "type", 10.0));
        expectedBids.add(new BidList("account", "type", 10.0));
        when(bidListService.findAll()).thenReturn(expectedBids);

        //when
        ResponseEntity<List<BidList>> response = bidListRestController.getAllBids();

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedBids, response.getBody());
    }


    @Test
    public void getAllBidNegativeTest() throws EmptyListException {

        //given
        when(bidListService.findAll()).thenReturn(new ArrayList<>());

        //when
        ResponseEntity<List<BidList>> response = bidListRestController.getAllBids();

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void getBidTest() throws NotExistingException {

        //given
        BidList expectedBid = new BidList("account", "type", 10.0);
        when(bidListService.findById(1)).thenReturn(expectedBid);

        //when
        ResponseEntity<BidList> response = bidListRestController.getBid(1);

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedBid, response.getBody());
    }


    @Test
    public void getBidNegativeTest() throws NotExistingException {

        //given
        when(bidListService.findById(1)).thenReturn(null);

        //when
        ResponseEntity<BidList> response = bidListRestController.getBid(1);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void addBidNegativeTest() {

        //given
        BidList bid = new BidList("", "type", 1.0);

        //when
        ResponseEntity<String> response = bidListRestController.addBid(bid);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Account is mandatory", response.getBody());
    }

    @Test
    public void addBidNegativeTest2() {

        //given
        BidList bid = new BidList("account", "", 1.0);

        //when
        ResponseEntity<String> response = bidListRestController.addBid(bid);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Type is mandatory", response.getBody());
    }

    @Test
    public void addBidNegativeTest3() {

        //given
        BidList bid = new BidList("account", "type", null);

        //when
        ResponseEntity<String> response = bidListRestController.addBid(bid);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bid Quantity is mandatory", response.getBody());
    }

    @Test
    public void addBidTest() {

        //given
        BidList bid = new BidList("account", "type", 1.0);
        bid.setId(1);

        //when
        ResponseEntity<String> response = bidListRestController.addBid(bid);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Bid with id 1 created !", response.getBody());
    }



    @Test
    public void updateBidTest() throws NotExistingException {

        //given
        BidList bid = new BidList("account", "type", 10.0);
        when(bidListService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = bidListRestController.updateBid(1, bid);

        //then
        verify(bidListService).updateBid(1, bid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void updateBidNegativeTest() throws NotExistingException {

        //given
        BidList bid = new BidList("account", "type", 10.0);
        when(bidListService.existsById(1)).thenReturn(false);

        //when
        ResponseEntity<String> response = bidListRestController.updateBid(1, bid);

        //then
        verify(bidListService, never()).updateBid(1, bid);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void updateBidNegativeTest2() throws NotExistingException {

        //given
        BidList bid = new BidList("", "type", 1.0);
        when(bidListService.existsById(anyInt())).thenReturn(true);

        //when
        ResponseEntity<String> response = bidListRestController.updateBid(1, bid);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Account is mandatory", response.getBody());
    }

    @Test
    public void updateBidNegativeTest3() throws NotExistingException {

        //given
        BidList bid = new BidList("account", "", 1.0);
        when(bidListService.existsById(anyInt())).thenReturn(true);


        //when
        ResponseEntity<String> response = bidListRestController.updateBid(1, bid);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Type is mandatory", response.getBody());
    }

    @Test
    public void updateBidNegativeTest4() throws NotExistingException {

        //given
        BidList bid = new BidList("account", "type", null);
        when(bidListService.existsById(anyInt())).thenReturn(true);


        //when
        ResponseEntity<String> response = bidListRestController.updateBid(1, bid);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bid Quantity is mandatory", response.getBody());
    }


    @Test
    public void deleteBidTest() throws NotExistingException {

        // given
        int bidId = 1;
        BidList bid = new BidList();
        bid.setId(bidId);
        when(bidListService.existsById(bidId)).thenReturn(true);

        // when
        ResponseEntity<String> response = bidListRestController.deleteBid(bidId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bidListService).existsById(bidId);
        verify(bidListService).deleteById(bidId);
    }


    @Test
    public void deleteBidNegativeTest() throws NotExistingException {

        // given
        int bidId = 1;
        when(bidListService.existsById(bidId)).thenReturn(false);

        // when
        ResponseEntity<String> response = bidListRestController.deleteBid(bidId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bidListService).existsById(bidId);
    }
}