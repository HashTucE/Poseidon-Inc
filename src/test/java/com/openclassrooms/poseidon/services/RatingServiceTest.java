package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.RatingRepository;
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
public class RatingServiceTest {


    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    private final Rating rating = new Rating("mood", "sand", "fitch", 1);



    @Test
    void findAllTest() throws EmptyListException {

        //given
        List<Rating> ratings = Arrays.asList(new Rating(), new Rating(), new Rating());
        when(ratingRepository.findAll()).thenReturn(ratings);

        //when
        List<Rating> result = ratingService.findAll();

        //then
        assertEquals(ratings, result);
    }


    @Test
    void findAllNegativeTest() {

        //given
        when(ratingRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        try {
            ratingService.findAll();
        } catch (EmptyListException e) {

            //then
            assertEquals("There is no rating yet.", e.getMessage());
        }
    }


    @Test
    public void existsByIdTest() throws NotExistingException {

        //given
        int tradeId = 1;
        when(ratingRepository.existsById(tradeId)).thenReturn(true);

        //when
        boolean exists = ratingService.existsById(tradeId);

        //then
        assertTrue(exists);
        verify(ratingRepository, times(1)).existsById(tradeId);
    }


    @Test
    public void existsByIdNegativeTest() {

        //given
        int id = 1;
        when(ratingRepository.existsById(id)).thenReturn(false);

        //when
        try {
            ratingService.existsById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("rating with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void findByIdTest() throws NotExistingException {


        //given
        int id = 1;
        when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));

        // when
        ratingService.findById(id);

        //then
        verify(ratingRepository, times(1)).findById(1);
        assertEquals(rating, ratingService.findById(id));
    }


    @Test
    void findByIdNegativeTest() {


        //given
        int id = 1;
        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        //when
        try {
            ratingService.findById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("rating with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void addRatingTest() {

        //given//when
        ratingService.addRating(rating);

        //then
        verify(ratingRepository, times(1)).save(any());
    }


    @Test
    void updateRatingTest() throws NotExistingException {

        //given
        Rating optionalRating = new Rating("mood", "sand", "fitch", 1);

        //when
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        ratingService.updateRating(1,optionalRating);

        //then
        verify(ratingRepository, times(1)).save(any());
    }


    @Test
    void updateRatingNegativeTest() {

        //given
        when(ratingRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            ratingService.updateRating(1, rating);
        } catch (NotExistingException e) {
            //then
            assertEquals("rating with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void deleteByIdTest() throws NotExistingException {

        //given//when
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        ratingService.deleteById(1);

        //then
        verify(ratingRepository, times(1)).delete(any());
    }


    @Test
    void deleteByIdNegativeTest() {

        //given
        when(ratingRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            ratingService.deleteById(1);
        } catch (NotExistingException e) {
            //then
            assertEquals("rating with id number 1 does not exist !", e.getMessage());
        }
    }
}
