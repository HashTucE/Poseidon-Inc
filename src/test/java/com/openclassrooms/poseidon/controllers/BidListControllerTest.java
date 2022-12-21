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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidListControllerTest {


    @InjectMocks
    private BidListController bidListController;

    @Mock
    private BidListService bidListService;

    @Mock
    BindingResult result;

    @Mock
    private Model model;


    @Test
    void homeTest() throws EmptyListException {

        //given
        List<BidList> bidList = Arrays.asList(new BidList(), new BidList());
        when(bidListService.findAll()).thenReturn(bidList);

        //when
        String result = bidListController.home(model);

        //then
        assertEquals("bidList/list", result);
        verify(model).addAttribute("bidList", bidList);
    }


    @Test
    void homeNegativeTest() throws EmptyListException {

        //given
        String alertMessage = "There is no bid yet.";
        doThrow(new EmptyListException("bid")).when(bidListService).findAll();

        //when
        String result = bidListController.home(model);

        //then
        assertEquals("bidList/list", result);
        verify(model).addAttribute("alertMessage", alertMessage);
    }


    @Test
    public void addBidFormTest() {
        //given
        BidList bid = new BidList();

        //when
        String result = bidListController.addBidForm(bid);

        //then
        assertEquals("bidList/add", result);
    }


    @Test
    public void validateTest() {


        //given
        BidList bid = new BidList();
        when(result.hasErrors()).thenReturn(false);

        //when
        String string = bidListController.validate(bid, result);

        //then
        assertEquals("redirect:/bidList/list", string);
        verify(bidListService).addBid(bid);
    }


    @Test
    public void validateNegativeTest() {


        //given
        BidList bid = new BidList();
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = bidListController.validate(bid, result);

        //then
        assertEquals("bidList/add", string);
    }


    @Test
    public void showUpdateFormTest() throws NotExistingException {

        //given
        int id = 123;
        BidList bid = new BidList();
        when(bidListService.findById(id)).thenReturn(bid);

        //when
        String result = bidListController.showUpdateForm(id, model);

        //then
        assertEquals("bidList/update", result);
        verify(model).addAttribute("bid", bid);
    }


    @Test
    public void updateBidTest() throws NotExistingException {

        //given
        BidList bid = new BidList("account", "type", 10.0);
        bid.setId(1);

        //when
        String string = bidListController.updateBid(1, bid, result);

        //then
        assertEquals("redirect:/bidList/list", string);
        verify(bidListService, times(1)).updateBid(1, bid);
    }


    @Test
    public void updateBidNegativeTest() throws NotExistingException {

        //given
        BidList bid = new BidList("account", "type", 10.0);
        bid.setId(1);
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = bidListController.updateBid(1, bid, result);

        //then
        assertEquals("bidList/update", string);
    }


    @Test
    public void deleteBidTest() throws NotExistingException {

        //given//when
        String string = bidListController.deleteBid(1);

        //then
        assertEquals("redirect:/bidList/list", string);
        verify(bidListService, times(1)).deleteById(1);
    }
}
