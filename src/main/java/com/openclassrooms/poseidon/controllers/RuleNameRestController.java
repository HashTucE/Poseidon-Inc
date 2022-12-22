package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.constants.Log;
import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RuleNameService;
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
public class RuleNameRestController {



    private static final Logger log = LogManager.getLogger(RuleNameRestController.class);



    private final RuleNameService ruleNameService;

    public RuleNameRestController(RuleNameService ruleNameService) {
        this.ruleNameService = ruleNameService;
    }


    //GET, POST, PUT, DELETE CONTROLLERS//



    /**
     * Find all rules
     * @return rule with HTTP code 200 OK
     */
    @GetMapping("/rule/all")
    public ResponseEntity<List<RuleName>> getAllRules() throws EmptyListException {
        List<RuleName> rules = ruleNameService.findAll();
        if (rules.isEmpty()) {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(rules, HttpStatus.FOUND);
        }
    }


    /**
     * Find a rule by id
     * @param id of the rule
     * @return rule with HTTP code 200 found
     */
    @GetMapping("/rule")
    public ResponseEntity<RuleName> getRule(@RequestParam int id) throws NotExistingException {
        RuleName rule = ruleNameService.findById(id);
        if (rule != null) {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(rule, HttpStatus.FOUND);
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Create a rule
     * @param rule rule
     * @return rule with HTTP code 201 created
     */
    @PostMapping("/rule")
    public ResponseEntity<String> addRule(@RequestBody RuleName rule) {

        if (rule.getName().isBlank()) {
            return new ResponseEntity<>("Name is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (rule.getDescription().isBlank()) {
            return new ResponseEntity<>("Description is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (rule.getJson().isBlank()) {
            return new ResponseEntity<>("Json is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (rule.getTemplate().isBlank()) {
            return new ResponseEntity<>("Template is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (rule.getSqlStr().isBlank()) {
            return new ResponseEntity<>("Sql is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (rule.getSqlPart().isBlank()) {
            return new ResponseEntity<>("SqlPart is mandatory", HttpStatus.BAD_REQUEST);
        }
        ruleNameService.addRuleName(rule);
        log.info(Log.OBJECT_CREATED);
        return new ResponseEntity<>("Rule created !", HttpStatus.CREATED);
    }


    /**
     * Update a rule
     * @param id of the rule and rule object
     * @return HTTP code 200 OK with confirmation string
     */
    @PutMapping("/rule")
    public ResponseEntity<String> updateRule(@RequestParam int id, @RequestBody RuleName rule) throws NotExistingException {

        if(ruleNameService.existsById(id)) {

            if (rule.getName().isBlank()) {
                return new ResponseEntity<>("Name is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (rule.getDescription().isBlank()) {
                return new ResponseEntity<>("Description is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (rule.getJson().isBlank()) {
                return new ResponseEntity<>("Json is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (rule.getTemplate().isBlank()) {
                return new ResponseEntity<>("Template is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (rule.getSqlStr().isBlank()) {
                return new ResponseEntity<>("Sql is mandatory", HttpStatus.BAD_REQUEST);
            }
            if (rule.getSqlPart().isBlank()) {
                return new ResponseEntity<>("SqlPart is mandatory", HttpStatus.BAD_REQUEST);
            }
            ruleNameService.updateRuleName(id, rule);
            log.info(Log.OBJECT_MODIFIED);
            return ResponseEntity.ok().body("Rule with id " + id + " updated !");

        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete a rule
     * @param id of the rule
     * @return HTTP code 200 OK with confirmation string
     */
    @DeleteMapping("/rule")
    public ResponseEntity<String> deleteRule(@RequestParam int id) throws NotExistingException {

        if(ruleNameService.existsById(id)) {
            ruleNameService.deleteById(id);
            log.info(Log.OBJECT_DELETED);
            return ResponseEntity.ok().body("Rule with id " + id + " deleted !");
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
