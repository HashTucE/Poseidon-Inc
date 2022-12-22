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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingRestControllerTest {



    @InjectMocks
    private RatingRestController ratingRestController;

    @Mock
    private RatingService ratingService;



    @Test
    public void getAllRatingTest() throws EmptyListException {

        //given
        List<Rating> expectedRatings = new ArrayList<>();
        expectedRatings.add(new Rating("a", "a", "a", 1));
        expectedRatings.add(new Rating("a", "a", "a", 1));
        when(ratingService.findAll()).thenReturn(expectedRatings);

        //when
        ResponseEntity<List<Rating>> response = ratingRestController.getAllRatings();

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedRatings, response.getBody());
    }


    @Test
    public void getAllRatingNegativeTest() throws EmptyListException {

        //given
        when(ratingService.findAll()).thenReturn(new ArrayList<>());

        //when
        ResponseEntity<List<Rating>> response = ratingRestController.getAllRatings();

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void getRatingTest() throws NotExistingException {

        //given
        Rating expectedRating = new Rating("a", "a", "a", 1);
        when(ratingService.findById(1)).thenReturn(expectedRating);

        //when
        ResponseEntity<Rating> response = ratingRestController.getRating(1);

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedRating, response.getBody());
    }


    @Test
    public void getRatingNegativeTest() throws NotExistingException {

        //given
        when(ratingService.findById(1)).thenReturn(null);

        //when
        ResponseEntity<Rating> response = ratingRestController.getRating(1);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void addRatingNegativeTest() {

        //given
        Rating rating = new Rating("", "a", "a", 1);

        //when
        ResponseEntity<String> response = ratingRestController.addRating(rating);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MoodysRating is mandatory", response.getBody());
    }

    @Test
    public void addRatingNegativeTest2() {

        //given
        Rating rating = new Rating("a", "", "a", 1);

        //when
        ResponseEntity<String> response = ratingRestController.addRating(rating);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("SandPRating is mandatory", response.getBody());
    }


    @Test
    public void addRatingNegativeTest3() {

        //given
        Rating rating = new Rating("a", "a", "", 1);

        //when
        ResponseEntity<String> response = ratingRestController.addRating(rating);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("FitchRating is mandatory", response.getBody());
    }


    @Test
    public void addRatingNegativeTest4() {

        //given
        Rating rating = new Rating("a", "a", "a", null);

        //when
        ResponseEntity<String> response = ratingRestController.addRating(rating);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Order is mandatory", response.getBody());
    }


    @Test
    public void addRatingTest() {

        //given
        Rating rating = new Rating("a", "a", "a", 1);

        //when
        ResponseEntity<String> response = ratingRestController.addRating(rating);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Rating created !", response.getBody());
    }


    @Test
    public void updateRatingTest() throws NotExistingException {

        //given
        Rating rating = new Rating("a", "a", "a", 1);
        when(ratingService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = ratingRestController.updateRating(1, rating);

        //then
        verify(ratingService).updateRating(1, rating);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void updateRatingNegativeTest() throws NotExistingException {

        //given
        Rating rating = new Rating("a", "a", "a", 1);
        when(ratingService.existsById(1)).thenReturn(false);

        //when
        ResponseEntity<String> response = ratingRestController.updateRating(1, rating);

        //then
        verify(ratingService, never()).updateRating(1, rating);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void updateRatingNegativeTest2() throws NotExistingException {

        //given
        Rating rating = new Rating("", "a", "a", 1);
        when(ratingService.existsById(anyInt())).thenReturn(true);


        //when
        ResponseEntity<String> response = ratingRestController.updateRating(1, rating);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MoodysRating is mandatory", response.getBody());
    }

    @Test
    public void updateRatingNegativeTest3() throws NotExistingException {

        //given
        Rating rating = new Rating("a", "", "a", 1);
        when(ratingService.existsById(anyInt())).thenReturn(true);


        //when
        ResponseEntity<String> response = ratingRestController.updateRating(1, rating);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("SandPRating is mandatory", response.getBody());
    }


    @Test
    public void updateRatingNegativeTest4() throws NotExistingException {

        //given
        Rating rating = new Rating("a", "a", "", 1);
        when(ratingService.existsById(anyInt())).thenReturn(true);


        //when
        ResponseEntity<String> response = ratingRestController.updateRating(1, rating);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("FitchRating is mandatory", response.getBody());
    }


    @Test
    public void updateRatingNegativeTest5() throws NotExistingException {

        //given
        Rating rating = new Rating("a", "a", "a", null);
        when(ratingService.existsById(anyInt())).thenReturn(true);


        //when
        ResponseEntity<String> response = ratingRestController.updateRating(1, rating);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Order is mandatory", response.getBody());
    }


    @Test
    public void deleteRatingTest() throws NotExistingException {

        // given
        int ratingId = 1;
        Rating rating = new Rating();
        rating.setId(ratingId);
        when(ratingService.existsById(ratingId)).thenReturn(true);

        // when
        ResponseEntity<String> response = ratingRestController.deleteRating(ratingId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ratingService).existsById(ratingId);
        verify(ratingService).deleteById(ratingId);
    }


    @Test
    public void deleteRatingNegativeTest() throws NotExistingException {

        // given
        int ratingId = 1;
        when(ratingService.existsById(ratingId)).thenReturn(false);

        // when
        ResponseEntity<String> response = ratingRestController.deleteRating(ratingId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(ratingService).existsById(ratingId);
    }
}