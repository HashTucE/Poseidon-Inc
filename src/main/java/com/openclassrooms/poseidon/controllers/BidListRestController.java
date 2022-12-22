package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.constants.Log;
import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.BidListService;
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
public class BidListRestController {



    private static final Logger log = LogManager.getLogger(BidListRestController.class);



    private final BidListService bidListService;

    public BidListRestController(BidListService bidListService) {
        this.bidListService = bidListService;
    }


    //GET, POST, PUT, DELETE CONTROLLERS//



    /**
     * Find all bids
     * @return bids with HTTP code 200 OK
     */
    @GetMapping("/bidList/all")
    public ResponseEntity<List<BidList>> getAllBids() throws EmptyListException {
        List<BidList> bids = bidListService.findAll();
        if (bids.isEmpty()) {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(bids, HttpStatus.FOUND);
        }
    }


    /**
     * Find a bid by id
     * @param id of the bid
     * @return Bid with HTTP code 200 found
     */
    @GetMapping("/bidList")
    public ResponseEntity<BidList> getBid(@RequestParam int id) throws NotExistingException {
        BidList bid = bidListService.findById(id);
        if (bid != null) {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(bid, HttpStatus.FOUND);
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Create a bid
     * @param bid bid
     * @return bid with HTTP code 201 created
     */
    @PostMapping("/bidList")
    public ResponseEntity<String> addBid(@RequestBody BidList bid) {

        if (bid.getAccount().isBlank()) {
            return new ResponseEntity<>("Account is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (bid.getType().isBlank()) {
            return new ResponseEntity<>("Type is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (bid.getBidQuantity() == null) {
            return new ResponseEntity<>("Bid Quantity is mandatory", HttpStatus.BAD_REQUEST);
        }
        bidListService.addBid(bid);
        log.info(Log.OBJECT_CREATED);
        return new ResponseEntity<>("Bid created !", HttpStatus.CREATED);
    }


    /**
     * Update a bid
     * @param id of the bid and bid object
     * @return HTTP code 200 OK with confirmation string
     */
    @PutMapping("/bidList")
    public ResponseEntity<String> updateBid(@RequestParam int id, @RequestBody BidList bid) throws NotExistingException {

        if(bidListService.existsById(id)) {

            if (bid.getAccount().isBlank()) {
                return new ResponseEntity<>("Account is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (bid.getType().isBlank()) {
                return new ResponseEntity<>("Type is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (bid.getBidQuantity() == null) {
                return new ResponseEntity<>("Bid Quantity is mandatory", HttpStatus.BAD_REQUEST);
            }
            bidListService.updateBid(id, bid);
            log.info(Log.OBJECT_MODIFIED);
            return ResponseEntity.ok().body("Bid with id " + id + " updated !");

        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete a bid
     * @param id of the bid
     * @return HTTP code 200 OK with confirmation string
     */
    @DeleteMapping("/bidList")
    public ResponseEntity<String> deleteBid(@RequestParam int id) throws NotExistingException {

        if(bidListService.existsById(id)) {
            bidListService.deleteById(id);
            log.info(Log.OBJECT_DELETED);
            return ResponseEntity.ok().body("Bid with id " + id + " deleted !");
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
