package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.TradeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {


    private static final Logger log = LogManager.getLogger(TradeService.class);

    @Autowired
    private TradeRepository tradeRepository;



    public List<Trade> findAll() throws EmptyListException {

        if(tradeRepository.findAll().isEmpty()) {
            log.error("findAll Trade return an empty list");
            throw new EmptyListException("trade");
        }
        log.debug("findAll Trade from service called");
        return tradeRepository.findAll();
    }


    public boolean existsById(int id) throws NotExistingException {

        boolean isTradeExist = tradeRepository.existsById(id);
        if (!isTradeExist) {
            throw new NotExistingException("trade", id);
        }
        log.debug("existsById = " + id + " from Trade service called");
        return true;
    }



    public Trade findById(int id) throws NotExistingException {

        log.debug("findById = " + id + " from Trade service called");
        return tradeRepository.findById(id).orElseThrow(()-> new NotExistingException("trade", id));
    }



    public Trade addTrade(Trade trade) {

        tradeRepository.save(trade);
        log.debug("addTrade with id " + trade.getId() + " from service called");
        return trade;
    }



    public void updateTrade(int id, Trade trade) throws NotExistingException {

        Trade tradeToModify = findById(id);

        tradeToModify.setAccount(trade.getAccount());
        tradeToModify.setType(trade.getType());
        tradeToModify.setBuyQuantity(trade.getBuyQuantity());
        tradeRepository.save(tradeToModify);
        log.debug("updateTrade with id " + trade.getId() + " from service called with success");
    }



    public void deleteById(int id) throws NotExistingException {

        Trade tradeToDelete = findById(id);

        tradeRepository.delete(tradeToDelete);
        log.debug("deleteById = " + id + " from Trade service called with success");
    }
}
