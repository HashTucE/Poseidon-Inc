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


    /**
     * call findAll from repository
     * @return rating list
     * @throws EmptyListException e
     */
    public List<Rating> findAll() throws EmptyListException {

        if(ratingRepository.findAll().isEmpty()) {
            log.error("findAll Rating return an empty list");
            throw new EmptyListException("rating");
        }
        log.debug("findAll Rating from service called");
        return ratingRepository.findAll();
    }


    /**
     * check if an object exist by id
     * @param id id
     * @return boolean
     * @throws NotExistingException e
     */
    public boolean existsById(int id) throws NotExistingException {

        boolean isRatingExist = ratingRepository.existsById(id);
        if (!isRatingExist) {
            throw new NotExistingException("rating", id);
        }
        log.debug("existsById = " + id + " from Rating service called");
        return true;
    }


    /**
     * call findById from repository
     * @param id id
     * @return rating object
     * @throws NotExistingException e
     */
    public Rating findById(int id) throws NotExistingException {

        log.debug("findById = " + id + " from Rating service called");
        return ratingRepository.findById(id).orElseThrow(()-> new NotExistingException("rating", id));
    }


    /**
     * call save method from repository
     * @param rating rating
     * @return rating object
     */
    public Rating addRating(Rating rating) {

        ratingRepository.save(rating);
        log.debug("addRating with id " + rating.getId() + " from service called");
        return rating;
    }


    /**
     * update a rating calling save from repository
     * @param id id
     * @param rating rating
     * @throws NotExistingException e
     */
    public void updateRating(int id, Rating rating) throws NotExistingException {

        Rating ratingToModify = findById(id);

        ratingToModify.setMoodysRating(rating.getMoodysRating());
        ratingToModify.setSandPRating(rating.getSandPRating());
        ratingToModify.setFitchRating(rating.getFitchRating());
        ratingToModify.setOrderNumber(rating.getOrderNumber());
        ratingRepository.save(ratingToModify);
        log.debug("updateRating with id " + rating.getId() + " from service called with success");
    }


    /**
     * delete an object by id calling delete from repository
     * @param id id
     * @throws NotExistingException e
     */
    public void deleteById(int id) throws NotExistingException {

        Rating optionalRating = findById(id);

            ratingRepository.delete(optionalRating);
            log.debug("deleteById = " + id + " from Rating service called with success");
    }
}
