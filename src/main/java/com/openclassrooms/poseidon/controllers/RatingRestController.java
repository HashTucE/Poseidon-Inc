package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.constants.Log;
import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RatingService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingRestController {



    private static final Logger log = LogManager.getLogger(RatingRestController.class);



    private final RatingService ratingService;

    public RatingRestController(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    //GET, POST, PUT, DELETE CONTROLLERS//



    /**
     * Find all ratings
     * @return Rating with HTTP code 200 OK
     */
    @GetMapping("/rating/all")
    public ResponseEntity<List<Rating>> getAllRatings() throws EmptyListException {
        List<Rating> ratings = ratingService.findAll();
        if (ratings.isEmpty()) {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(ratings, HttpStatus.FOUND);
        }
    }


    /**
     * Find a rating by id
     * @param id of the rating
     * @return rating with HTTP code 200 found
     */
    @GetMapping("/rating")
    public ResponseEntity<Rating> getRating(@RequestParam int id) throws NotExistingException {
        Rating rating = ratingService.findById(id);
        if (rating != null) {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(rating, HttpStatus.FOUND);
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Create a rating
     * @param rating rating
     * @return rating with HTTP code 201 created
     */
    @PostMapping("/rating")
    public ResponseEntity<String> addRating(@RequestBody Rating rating) {

        if (rating.getMoodysRating().isBlank()) {
            return new ResponseEntity<>("MoodysRating is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (rating.getSandPRating().isBlank()) {
            return new ResponseEntity<>("SandPRating is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (rating.getFitchRating().isBlank()) {
            return new ResponseEntity<>("FitchRating is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (rating.getOrderNumber() == null) {
            return new ResponseEntity<>("Order is mandatory", HttpStatus.BAD_REQUEST);
        }
        ratingService.addRating(rating);
        log.info(Log.OBJECT_CREATED);
        return new ResponseEntity<>("Rating with id " + rating.getId() + " created !", HttpStatus.CREATED);
    }


    /**
     * Update a rating
     * @param id of the rating and rating object
     * @return HTTP code 200 OK with confirmation string
     */
    @PutMapping("/rating")
    public ResponseEntity<String> updateRating(@RequestParam int id, @RequestBody Rating rating) throws NotExistingException {

        if(ratingService.existsById(id)) {

            if (rating.getMoodysRating().isBlank()) {
                return new ResponseEntity<>("MoodysRating is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (rating.getSandPRating().isBlank()) {
                return new ResponseEntity<>("SandPRating is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (rating.getFitchRating().isBlank()) {
                return new ResponseEntity<>("FitchRating is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (rating.getOrderNumber() == null) {
                return new ResponseEntity<>("Order is mandatory", HttpStatus.BAD_REQUEST);
            }
            ratingService.updateRating(id, rating);
            log.info(Log.OBJECT_MODIFIED);
            return ResponseEntity.ok().body("Rating with id " + id + " updated !");

        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete a rating
     * @param id of the rating
     * @return HTTP code 200 OK with confirmation string
     */
    @DeleteMapping("/rating")
    public ResponseEntity<String> deleteRating(@RequestParam int id) throws NotExistingException {

        if(ratingService.existsById(id)) {
            ratingService.deleteById(id);
            log.info(Log.OBJECT_DELETED);
            return ResponseEntity.ok().body("Rating with id " + id + " deleted !");
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
