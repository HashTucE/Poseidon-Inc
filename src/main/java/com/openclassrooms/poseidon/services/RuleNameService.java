package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.RuleNameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameService {


    private static final Logger log = LogManager.getLogger(RuleNameService.class);

    @Autowired
    private RuleNameRepository ruleNameRepository;


    /**
     * call findAll from repository
     * @return ruleName list
     * @throws EmptyListException e
     */
    public List<RuleName> findAll() throws EmptyListException {

        if(ruleNameRepository.findAll().isEmpty()) {
            log.error("findAll RuleName return an empty list");
            throw new EmptyListException("rule");
        }
        log.debug("findAll RuleName from service called");
        return ruleNameRepository.findAll();
    }


    /**
     * check if an object exist by id
     * @param id id
     * @return boolean
     * @throws NotExistingException e
     */
    public boolean existsById(int id) throws NotExistingException {

        boolean isRuleExist = ruleNameRepository.existsById(id);
        if (!isRuleExist) {
            throw new NotExistingException("rule", id);
        }
        log.debug("existsById = " + id + " from RuleName service called");
        return true;
    }


    /**
     * call findById from repository
     * @param id id
     * @return ruleName object
     * @throws NotExistingException e
     */
    public RuleName findById(int id) throws NotExistingException {

        log.debug("findById = " + id + " from RuleName service called");
        return ruleNameRepository.findById(id).orElseThrow(()-> new NotExistingException("rule", id));
    }


    /**
     * call save method from repository
     * @param ruleName ruleName
     * @return ruleName object
     */
    public RuleName addRuleName(RuleName ruleName) {

        ruleNameRepository.save(ruleName);
        log.debug("addRuleName with id " + ruleName.getId() + " from service called");
        return ruleName;
    }


    /**
     * update a ruleName calling save from repository
     * @param id id
     * @param ruleName ruleName
     * @throws NotExistingException e
     */
    public void updateRuleName(int id, RuleName ruleName) throws NotExistingException {

        RuleName ruleNameToModify = findById(id);

        ruleNameToModify.setName(ruleName.getName());
        ruleNameToModify.setDescription(ruleName.getDescription());
        ruleNameToModify.setJson(ruleName.getJson());
        ruleNameToModify.setTemplate(ruleName.getTemplate());
        ruleNameToModify.setSqlStr(ruleName.getSqlStr());
        ruleNameToModify.setSqlPart(ruleName.getSqlPart());
        ruleNameRepository.save(ruleNameToModify);
        log.debug("updateRuleName with id " + ruleName.getId() + " from service called with success");
    }


    /**
     * delete an object by id calling delete from repository
     * @param id id
     * @throws NotExistingException e
     */
    public void deleteById(int id) throws NotExistingException {

        RuleName optionalRuleName = findById(id);

        ruleNameRepository.delete(optionalRuleName);
        log.debug("deleteById = " + id + " from RuleName service called with success");
    }
}
