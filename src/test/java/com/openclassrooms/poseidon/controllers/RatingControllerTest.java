package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RatingService;
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
public class RatingControllerTest {



    @InjectMocks
    private RatingController ratingController;

    @Mock
    private RatingService ratingService;

    @Mock
    BindingResult result;

    @Mock
    private Model model;


    @Test
    void homeTest() throws EmptyListException {

        //given
        List<Rating> ratings = Arrays.asList(new Rating(), new Rating());
        when(ratingService.findAll()).thenReturn(ratings);

        //when
        String result = ratingController.home(model);

        //then
        assertEquals("rating/list", result);
        verify(model).addAttribute("ratingList", ratings);
    }


    @Test
    void homeNegativeTest() throws EmptyListException {

        //given
        String alertMessage = "There is no rating yet.";
        doThrow(new EmptyListException("rating")).when(ratingService).findAll();

        //when
        String result = ratingController.home(model);

        //then
        assertEquals("rating/list", result);
        verify(model).addAttribute("alertMessage", alertMessage);
    }


    @Test
    public void addRatingFormTest() {
        //given
        Rating rating = new Rating();

        //when
        String result = ratingController.addRatingForm(rating);

        //then
        assertEquals("rating/add", result);
    }


    @Test
    public void validateTest() {


        //given
        Rating rating = new Rating();
        when(result.hasErrors()).thenReturn(false);

        //when
        String string = ratingController.validate(rating, result);

        //then
        assertEquals("redirect:/rating/list", string);
        verify(ratingService).addRating(rating);
    }


    @Test
    public void validateNegativeTest() {


        //given
        Rating rating = new Rating();
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = ratingController.validate(rating, result);

        //then
        assertEquals("rating/add", string);
    }


    @Test
    public void showUpdateFormTest() throws NotExistingException {

        //given
        int id = 123;
        Rating rating = new Rating();
        when(ratingService.findById(id)).thenReturn(rating);

        //when
        String string = ratingController.showUpdateForm(id, model);

        //then
        assertEquals("rating/update", string);
        verify(model).addAttribute("rating", rating);
    }


    @Test
    public void updateRatingTest() throws NotExistingException {

        //given
        Rating rating = new Rating("mood", "sand", "fitch", 10);
        rating.setId(1);

        //when
        String string = ratingController.updateRating(1, rating, result);

        //then
        assertEquals("redirect:/rating/list", string);
        verify(ratingService, times(1)).updateRating(1, rating);
    }


    @Test
    public void updateRatingNegativeTest() throws NotExistingException {

        //given
        Rating rating = new Rating("mood", "sand", "fitch", 10);
        rating.setId(1);
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = ratingController.updateRating(1, rating, result);

        //then
        assertEquals("rating/update", string);
    }


    @Test
    public void deleteRatingTest() throws NotExistingException {

        //given//when
        String string = ratingController.deleteRating(1);

        //then
        assertEquals("redirect:/rating/list", string);
        verify(ratingService, times(1)).deleteById(1);
    }
}
