package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.RatingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {


    private static final Logger log = LogManager.getLogger(RatingService.class);

    @Autowired
    private RatingRepository ratingRepository;



    public List<Rating> findAll() throws EmptyListException {

        if(ratingRepository.findAll().isEmpty()) {
            log.error("findAll Rating return an empty list");
            throw new EmptyListException("rating");
        }
        log.debug("findAll Rating from service called");
        return ratingRepository.findAll();
    }


    public boolean existsById(int id) throws NotExistingException {

        boolean isRatingExist = ratingRepository.existsById(id);
        if (!isRatingExist) {
            throw new NotExistingException("rating", id);
        }
        log.debug("existsById = " + id + " from Rating service called");
        return true;
    }



    public Rating findById(int id) throws NotExistingException {

        log.debug("findById = " + id + " from Rating service called");
        return ratingRepository.findById(id).orElseThrow(()-> new NotExistingException("rating", id));
    }



    public Rating addRating(Rating rating) {

        ratingRepository.save(rating);
        log.debug("addRating with id " + rating.getId() + " from service called");
        return rating;
    }



    public void updateRating(int id, Rating rating) throws NotExistingException {

        Rating ratingToModify = findById(id);

        ratingToModify.setMoodysRating(rating.getMoodysRating());
        ratingToModify.setSandPRating(rating.getSandPRating());
        ratingToModify.setFitchRating(rating.getFitchRating());
        ratingToModify.setOrderNumber(rating.getOrderNumber());
        ratingRepository.save(ratingToModify);
        log.debug("updateRating with id " + rating.getId() + " from service called with success");
    }



    public void deleteById(int id) throws NotExistingException {

        Rating optionalRating = findById(id);

            ratingRepository.delete(optionalRating);
            log.debug("deleteById = " + id + " from Rating service called with success");
    }
}
