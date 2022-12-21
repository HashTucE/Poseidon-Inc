package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.BidListRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {


    private static final Logger log = LogManager.getLogger(BidListService.class);

    @Autowired
    private BidListRepository bidListRepository;

    

    public List<BidList> findAll() throws EmptyListException {

        if(bidListRepository.findAll().isEmpty()) {
            log.error("findAll BidList return an empty list");
            throw new EmptyListException("bid");
        }
        log.debug("findAll Bid from service called");
        return bidListRepository.findAll();
    }


    public boolean existsById(int id) throws NotExistingException {

        boolean isBidExist = bidListRepository.existsById(id);
        if (!isBidExist) {
            throw new NotExistingException("bid", id);
        }
        log.debug("existsById = " + id + " from BidList service called with success");
        return true;
    }



    public BidList findById(int id) throws NotExistingException {

        log.debug("findById = " + id + " from BidList service called");
        return bidListRepository.findById(id).orElseThrow(()-> new NotExistingException("bid", id));
    }
    


    public BidList addBid(BidList bid) {

        bidListRepository.save(bid);
        log.debug("addBid with id " + bid.getId() + " from service called");
        return bid;
    }



    public void updateBid(int id, BidList bid) throws NotExistingException {

        BidList bidToModify = findById(id);

        bidToModify.setAccount(bid.getAccount());
        bidToModify.setType(bid.getType());
        bidToModify.setBidQuantity(bid.getBidQuantity());
        bidListRepository.save(bidToModify);
        log.debug("updateBid with id " + bid.getId() + " from service called with success");
    }



    public void deleteById(int id) throws NotExistingException {

        BidList optionalBid = findById(id);

        bidListRepository.delete(optionalBid);
        log.debug("deleteById = " + id + " from BidList service called with success");
    }
}
