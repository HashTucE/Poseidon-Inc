package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.RuleNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleNameServiceTest {


    @InjectMocks
    private RuleNameService ruleService;

    @Mock
    private RuleNameRepository ruleRepository;

    private final RuleName rule = new RuleName("a","b","a","a","a","a");



    @Test
    void findAllTest() throws EmptyListException {

        //given
        List<RuleName> ruleNames = Arrays.asList(new RuleName(), new RuleName(), new RuleName());
        when(ruleRepository.findAll()).thenReturn(ruleNames);

        //when
        List<RuleName> result = ruleService.findAll();

        //then
        assertEquals(ruleNames, result);
    }


    @Test
    void findAllNegativeTest() {

        //given
        when(ruleRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        try {
            ruleService.findAll();
        } catch (EmptyListException e) {

            //then
            assertEquals("There is no rule yet.", e.getMessage());
        }
    }


    @Test
    public void existsByIdTest() throws NotExistingException {

        //given
        int tradeId = 1;
        when(ruleRepository.existsById(tradeId)).thenReturn(true);

        //when
        boolean exists = ruleService.existsById(tradeId);

        //then
        assertTrue(exists);
        verify(ruleRepository, times(1)).existsById(tradeId);
    }


    @Test
    public void existsByIdNegativeTest() {

        //given
        int id = 1;
        when(ruleRepository.existsById(id)).thenReturn(false);

        //when
        try {
            ruleService.existsById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("rule with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void findByIdTest() throws NotExistingException {


        //given
        int id = 1;
        when(ruleRepository.findById(id)).thenReturn(Optional.of(rule));

        // when
        ruleService.findById(id);

        //then
        verify(ruleRepository, times(1)).findById(1);
        assertEquals(rule, ruleService.findById(id));
    }


    @Test
    void findByIdNegativeTest() {


        //given
        int id = 1;
        when(ruleRepository.findById(id)).thenReturn(Optional.empty());

        //when
        try {
            ruleService.findById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("rule with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void addRuleNameTest() {

        //given//when
        ruleService.addRuleName(rule);

        //then
        verify(ruleRepository, times(1)).save(any());
    }


    @Test
    void updateRuleNameTest() throws NotExistingException {

        //given
        RuleName optionalRule = new RuleName("a","a","a","a","a","a");

        //when
        when(ruleRepository.findById(1)).thenReturn(Optional.of(rule));
        ruleService.updateRuleName(1,optionalRule);

        //then
        verify(ruleRepository, times(1)).save(any());
    }


    @Test
    void updateRuleNameNegativeTest() {

        //given
        when(ruleRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            ruleService.updateRuleName(1, rule);
        } catch (NotExistingException e) {
            //then
            assertEquals("rule with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void deleteByIdTest() throws NotExistingException {

        //given//when
        when(ruleRepository.findById(1)).thenReturn(Optional.of(rule));
        ruleService.deleteById(1);

        //then
        verify(ruleRepository, times(1)).delete(any());
    }


    @Test
    void deleteByIdNegativeTest() {

        //given
        when(ruleRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            ruleService.deleteById(1);
        } catch (NotExistingException e) {
            //then
            assertEquals("rule with id number 1 does not exist !", e.getMessage());
        }
    }
}
