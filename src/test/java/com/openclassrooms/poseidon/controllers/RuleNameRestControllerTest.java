package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RuleNameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleNameRestControllerTest {



    @InjectMocks
    private RuleNameRestController ruleNameRestController;

    @Mock
    private RuleNameService ruleNameService;



    @Test
    public void getAllRulesTest() throws EmptyListException {

        //given
        List<RuleName> expectedRules = new ArrayList<>();
        expectedRules.add(new RuleName("a", "a", "a", "a", "a", "a"));
        expectedRules.add(new RuleName("a", "a", "a", "a", "a", "a"));
        when(ruleNameService.findAll()).thenReturn(expectedRules);

        //when
        ResponseEntity<List<RuleName>> response = ruleNameRestController.getAllRules();

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedRules, response.getBody());
    }


    @Test
    public void getAllRulesNegativeTest() throws EmptyListException {

        //given
        when(ruleNameService.findAll()).thenReturn(new ArrayList<>());

        //when
        ResponseEntity<List<RuleName>> response = ruleNameRestController.getAllRules();

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void getRuleTest() throws NotExistingException {

        //given
        RuleName expectedRule = new RuleName("a", "a", "a", "a", "a", "a");
        when(ruleNameService.findById(1)).thenReturn(expectedRule);

        //when
        ResponseEntity<RuleName> response = ruleNameRestController.getRule(1);

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedRule, response.getBody());
    }


    @Test
    public void getRuleNegativeTest() throws NotExistingException {

        //given
        when(ruleNameService.findById(1)).thenReturn(null);

        //when
        ResponseEntity<RuleName> response = ruleNameRestController.getRule(1);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void addRuleTest() {

        //given
        RuleName rule = new RuleName("a", "a", "a", "a", "a", "a");

        //when
        ResponseEntity<String> response = ruleNameRestController.addRule(rule);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Rule created !", response.getBody());
    }


    @Test
    public void addRuleNegativeTest() {

        //given
        RuleName rule = new RuleName("", "a", "a", "a", "a", "a");

        //when
        ResponseEntity<String> response = ruleNameRestController.addRule(rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Name is mandatory", response.getBody());
    }


    @Test
    public void addRuleNegativeTest2() {

        //given
        RuleName rule = new RuleName("a", "", "a", "a", "a", "a");

        //when
        ResponseEntity<String> response = ruleNameRestController.addRule(rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Description is mandatory", response.getBody());
    }


    @Test
    public void addRuleNegativeTest3() {

        //given
        RuleName rule = new RuleName("a", "a", "", "a", "a", "a");

        //when
        ResponseEntity<String> response = ruleNameRestController.addRule(rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Json is mandatory", response.getBody());
    }


    @Test
    public void addRuleNegativeTest4() {

        //given
        RuleName rule = new RuleName("a", "a", "a", "", "a", "a");

        //when
        ResponseEntity<String> response = ruleNameRestController.addRule(rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Template is mandatory", response.getBody());
    }


    @Test
    public void addRuleNegativeTest5() {

        //given
        RuleName rule = new RuleName("a", "a", "a", "a", "", "a");

        //when
        ResponseEntity<String> response = ruleNameRestController.addRule(rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sql is mandatory", response.getBody());
    }


    @Test
    public void addRuleNegativeTest6() {

        //given
        RuleName rule = new RuleName("a", "a", "a", "a", "a", "");

        //when
        ResponseEntity<String> response = ruleNameRestController.addRule(rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("SqlPart is mandatory", response.getBody());
    }


    @Test
    public void updateRuleTest() throws NotExistingException {

        //given
        RuleName rule = new RuleName("a", "a", "a", "a", "a", "a");
        when(ruleNameService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = ruleNameRestController.updateRule(1, rule);

        //then
        verify(ruleNameService).updateRuleName(1, rule);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void updateRuleNegativeTest() throws NotExistingException {

        //given
        RuleName rule = new RuleName("a", "a", "a", "a", "a", "a");
        when(ruleNameService.existsById(1)).thenReturn(false);

        //when
        ResponseEntity<String> response = ruleNameRestController.updateRule(1, rule);

        //then
        verify(ruleNameService, never()).updateRuleName(1, rule);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void updateRuleNegativeTest2() throws NotExistingException {

        //given
        RuleName rule = new RuleName("", "a", "a", "a", "a", "a");
        when(ruleNameService.existsById(anyInt())).thenReturn(true);

        //when
        ResponseEntity<String> response = ruleNameRestController.updateRule(1, rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Name is mandatory", response.getBody());
    }


    @Test
    public void updateRuleNegativeTest3() throws NotExistingException {

        //given
        RuleName rule = new RuleName("a", "", "a", "a", "a", "a");
        when(ruleNameService.existsById(anyInt())).thenReturn(true);

        //when
        ResponseEntity<String> response = ruleNameRestController.updateRule(1, rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Description is mandatory", response.getBody());
    }


    @Test
    public void updateRuleNegativeTest4() throws NotExistingException {

        //given
        RuleName rule = new RuleName("a", "a", "", "a", "a", "a");
        when(ruleNameService.existsById(anyInt())).thenReturn(true);

        //when
        ResponseEntity<String> response = ruleNameRestController.updateRule(1, rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Json is mandatory", response.getBody());
    }


    @Test
    public void updateRuleNegativeTest5() throws NotExistingException {

        //given
        RuleName rule = new RuleName("a", "a", "a", "", "a", "a");
        when(ruleNameService.existsById(anyInt())).thenReturn(true);

        //when
        ResponseEntity<String> response = ruleNameRestController.updateRule(1, rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Template is mandatory", response.getBody());
    }


    @Test
    public void updateRuleNegativeTest6() throws NotExistingException {

        //given
        RuleName rule = new RuleName("a", "a", "a", "a", "", "a");
        when(ruleNameService.existsById(anyInt())).thenReturn(true);

        //when
        ResponseEntity<String> response = ruleNameRestController.updateRule(1, rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sql is mandatory", response.getBody());
    }


    @Test
    public void updateRuleNegativeTest7() throws NotExistingException {

        //given
        RuleName rule = new RuleName("a", "a", "a", "a", "a", "");
        when(ruleNameService.existsById(anyInt())).thenReturn(true);

        //when
        ResponseEntity<String> response = ruleNameRestController.updateRule(1, rule);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("SqlPart is mandatory", response.getBody());
    }


    @Test
    public void deleteRuleTest() throws NotExistingException {

        // given
        int ruleId = 1;
        RuleName rule = new RuleName();
        rule.setId(ruleId);
        when(ruleNameService.existsById(ruleId)).thenReturn(true);

        // when
        ResponseEntity<String> response = ruleNameRestController.deleteRule(ruleId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ruleNameService).existsById(ruleId);
        verify(ruleNameService).deleteById(ruleId);
    }


    @Test
    public void deleteRuleNegativeTest() throws NotExistingException {

        // given
        int ruleId = 1;
        when(ruleNameService.existsById(ruleId)).thenReturn(false);

        // when
        ResponseEntity<String> response = ruleNameRestController.deleteRule(ruleId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(ruleNameService).existsById(ruleId);
    }
}
