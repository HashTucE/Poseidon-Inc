package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RuleNameService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class RuleNameController {


    private final RuleNameService ruleNameService;

    public RuleNameController(RuleNameService ruleNameService) {
        this.ruleNameService = ruleNameService;
    }



    @RequestMapping("/ruleName/list")
    public String home(Model model) {

        try {
            model.addAttribute("ruleList", ruleNameService.findAll());
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
        }
        return "ruleName/list";
    }



    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName rule) {

        return "ruleName/add";
    }



    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result) {

        if (!result.hasErrors()) {
            ruleNameService.addRuleName(ruleName);
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }



    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        RuleName ruleName = ruleNameService.findById(id);
        model.addAttribute("ruleName", ruleName);

        return "ruleName/update";
    }



    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result) throws NotExistingException {
        if (!result.hasErrors()) {
            ruleNameService.updateRuleName(id, ruleName);
            return "redirect:/ruleName/list";
        }
        return "ruleName/update";
    }



    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) throws NotExistingException {

        ruleNameService.deleteById(id);
        return "redirect:/ruleName/list";
    }
}
