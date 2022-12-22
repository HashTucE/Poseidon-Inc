package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.constants.Log;
import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.TradeService;
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
public class TradeRestController {



    private static final Logger log = LogManager.getLogger(TradeRestController.class);



    private final TradeService tradeService;

    public TradeRestController(TradeService tradeService) {
        this.tradeService = tradeService;
    }


    //GET, POST, PUT, DELETE CONTROLLERS//



    /**
     * Find all trades
     * @return Trade with HTTP code 200 OK
     */
    @GetMapping("/trade/all")
    public ResponseEntity<List<Trade>> getAllTrade() throws EmptyListException {
        List<Trade> trades = tradeService.findAll();
        if (trades.isEmpty()) {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(trades, HttpStatus.FOUND);
        }
    }


    /**
     * Find a trade by id
     * @param id of the trade
     * @return Trade with HTTP code 200 found
     */
    @GetMapping("/trade")
    public ResponseEntity<Trade> getTrade(@RequestParam int id) throws NotExistingException {
        Trade trade = tradeService.findById(id);
        if (trade != null) {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(trade, HttpStatus.FOUND);
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Create a trade
     * @param trade trade
     * @return Trade with HTTP code 201 created
     */
    @PostMapping("/trade")
    public ResponseEntity<String> addTrade(@RequestBody Trade trade) {

        if (trade.getAccount().isBlank()) {
            return new ResponseEntity<>("Account is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (trade.getType().isBlank()) {
            return new ResponseEntity<>("Type is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (trade.getBuyQuantity() == null) {
            return new ResponseEntity<>("BuyQuantity is mandatory", HttpStatus.BAD_REQUEST);
        }
        tradeService.addTrade(trade);
        log.info(Log.OBJECT_CREATED);
        return new ResponseEntity<>("Trade created !", HttpStatus.CREATED);
    }


    /**
     * Update a trade
     * @param id of the trade and trade object
     * @return HTTP code 200 OK with confirmation string
     */
    @PutMapping("/trade")
    public ResponseEntity<String> updateTrade(@RequestParam int id, @RequestBody Trade trade) throws NotExistingException {

        if(tradeService.existsById(id)) {

            if (trade.getAccount().isBlank()) {
                return new ResponseEntity<>("Account is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (trade.getType().isBlank()) {
                return new ResponseEntity<>("Type is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (trade.getBuyQuantity() == null) {
                return new ResponseEntity<>("BuyQuantity is mandatory", HttpStatus.BAD_REQUEST);
            }
            tradeService.updateTrade(id, trade);
            log.info(Log.OBJECT_MODIFIED);
            return ResponseEntity.ok().body("Trade with id " + id + " updated !");

        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete a trade
     * @param id of the trade
     * @return HTTP code 200 OK with confirmation string
     */
    @DeleteMapping("/trade")
    public ResponseEntity<String> deleteTrade(@RequestParam int id) throws NotExistingException {

        if(tradeService.existsById(id)) {
            tradeService.deleteById(id);
            log.info(Log.OBJECT_DELETED);
            return ResponseEntity.ok().body("Trade with id " + id + " deleted !");
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
