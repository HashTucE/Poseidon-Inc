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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleNameControllerTest {



    @InjectMocks
    private RuleNameController ruleNameController;

    @Mock
    private RuleNameService ruleNameService;

    @Mock
    BindingResult result;

    @Mock
    private Model model;


    @Test
    void homeTest() throws EmptyListException {

        //given
        List<RuleName> ruleName = Arrays.asList(new RuleName(), new RuleName());
        when(ruleNameService.findAll()).thenReturn(ruleName);

        //when
        String result = ruleNameController.home(model);

        //then
        assertEquals("ruleName/list", result);
        verify(model).addAttribute("ruleList", ruleName);
    }


    @Test
    void homeNegativeTest() throws EmptyListException {

        //given
        String alertMessage = "There is no rule yet.";
        doThrow(new EmptyListException("rule")).when(ruleNameService).findAll();

        //when
        String result = ruleNameController.home(model);

        //then
        assertEquals("ruleName/list", result);
        verify(model).addAttribute("alertMessage", alertMessage);
    }


    @Test
    public void addRuleNameFormTest() {

        //given
        RuleName ruleName = new RuleName();

        //when
        String result = ruleNameController.addRuleForm(ruleName);

        //then
        assertEquals("ruleName/add", result);
    }


    @Test
    public void validateTest() {

        //given
        RuleName ruleName = new RuleName();
        when(result.hasErrors()).thenReturn(false);

        //when
        String string = ruleNameController.validate(ruleName, result);

        //then
        assertEquals("redirect:/ruleName/list", string);
        verify(ruleNameService).addRuleName(ruleName);
    }


    @Test
    public void validateNegativeTest() {

        //given
        RuleName ruleName = new RuleName();
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = ruleNameController.validate(ruleName, result);

        //then
        assertEquals("ruleName/add", string);
    }


    @Test
    public void showUpdateFormTest() throws NotExistingException {

        //given
        int id = 123;
        RuleName ruleName = new RuleName();
        when(ruleNameService.findById(id)).thenReturn(ruleName);

        //when
        String string = ruleNameController.showUpdateForm(id, model);

        //then
        assertEquals("ruleName/update", string);
        verify(model).addAttribute("ruleName", ruleName);
    }


    @Test
    public void updateRuleNameTest() throws NotExistingException {

        //given
        RuleName ruleName = new RuleName("a", "a", "a", "a", "a", "a");
        ruleName.setId(1);

        //when
        String string = ruleNameController.updateRuleName(1, ruleName, result);

        //then
        assertEquals("redirect:/ruleName/list", string);
        verify(ruleNameService, times(1)).updateRuleName(1, ruleName);
    }


    @Test
    public void updateRuleNameNegativeTest() throws NotExistingException {

        //given
        RuleName ruleName = new RuleName("a", "a", "a", "a", "a", "a");
        ruleName.setId(1);
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = ruleNameController.updateRuleName(1, ruleName, result);

        //then
        assertEquals("ruleName/update", string);
    }


    @Test
    public void deleteRuleNameTest() throws NotExistingException {

        //given//when
        String string = ruleNameController.deleteRuleName(1);

        //then
        assertEquals("redirect:/ruleName/list", string);
        verify(ruleNameService, times(1)).deleteById(1);
    }
}
