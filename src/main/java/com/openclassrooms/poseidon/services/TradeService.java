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


    /**
     * call findAll from repository
     * @return trade list
     * @throws EmptyListException e
     */
    public List<Trade> findAll() throws EmptyListException {

        if(tradeRepository.findAll().isEmpty()) {
            log.error("findAll Trade return an empty list");
            throw new EmptyListException("trade");
        }
        log.debug("findAll Trade from service called");
        return tradeRepository.findAll();
    }


    /**
     * check if an object exist by id
     * @param id id
     * @return boolean
     * @throws NotExistingException e
     */
    public boolean existsById(int id) throws NotExistingException {

        boolean isTradeExist = tradeRepository.existsById(id);
        if (!isTradeExist) {
            throw new NotExistingException("trade", id);
        }
        log.debug("existsById = " + id + " from Trade service called");
        return true;
    }


    /**
     * call findById from repository
     * @param id id
     * @return trade object
     * @throws NotExistingException e
     */
    public Trade findById(int id) throws NotExistingException {

        log.debug("findById = " + id + " from Trade service called");
        return tradeRepository.findById(id).orElseThrow(()-> new NotExistingException("trade", id));
    }


    /**
     * call save method from repository
     * @param trade trade
     * @return trade object
     */
    public Trade addTrade(Trade trade) {

        tradeRepository.save(trade);
        log.debug("addTrade with id " + trade.getId() + " from service called");
        return trade;
    }


    /**
     * update a trade calling save from repository
     * @param id id
     * @param trade trade
     * @throws NotExistingException e
     */
    public void updateTrade(int id, Trade trade) throws NotExistingException {

        Trade tradeToModify = findById(id);

        tradeToModify.setAccount(trade.getAccount());
        tradeToModify.setType(trade.getType());
        tradeToModify.setBuyQuantity(trade.getBuyQuantity());
        tradeRepository.save(tradeToModify);
        log.debug("updateTrade with id " + trade.getId() + " from service called with success");
    }


    /**
     * delete an object by id calling delete from repository
     * @param id id
     * @throws NotExistingException e
     */
    public void deleteById(int id) throws NotExistingException {

        Trade tradeToDelete = findById(id);

        tradeRepository.delete(tradeToDelete);
        log.debug("deleteById = " + id + " from Trade service called with success");
    }
}
