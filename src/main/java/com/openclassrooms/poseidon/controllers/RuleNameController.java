package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.RuleName;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RuleNameService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger log = LogManager.getLogger(RuleNameController.class);


    /**
     * display the ruleName list
     * @param model model
     * @return string path
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {

        try {
            model.addAttribute("ruleList", ruleNameService.findAll());
            log.info("display ruleName list page");
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
            log.info("display ruleName list page with empty list");
        }
        return "ruleName/list";
    }



    /**
     * display add ruleName form
     * @return string path
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName rule) {

        log.info("display add ruleName page");
        return "ruleName/add";
    }



    /**
     * redirect after add ruleName
     * @param ruleName ruleName
     * @param result result
     * @return string path
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result) {

        if (!result.hasErrors()) {
            ruleNameService.addRuleName(ruleName);
            log.info("validate ruleName");
            return "redirect:/ruleName/list";
        }
        log.error("can't validate ruleName");
        return "ruleName/add";
    }



    /**
     * display update ruleName form
     * @param id id
     * @param model model
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        RuleName ruleName = ruleNameService.findById(id);
        model.addAttribute("ruleName", ruleName);

        log.info("display update ruleName page");
        return "ruleName/update";
    }



    /**
     * redirect after update ruleName
     * @param id id
     * @param ruleName ruleName
     * @param result result
     * @return string path
     * @throws NotExistingException e
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result) throws NotExistingException {
        if (!result.hasErrors()) {
            ruleNameService.updateRuleName(id, ruleName);
            log.info("update ruleName");
            return "redirect:/ruleName/list";
        }
        log.error("can't update ruleName");
        return "ruleName/update";
    }



    /**
     * redirect after delete ruleName
     * @param id id
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) throws NotExistingException {

        ruleNameService.deleteById(id);
        log.info("delete ruleName");
        return "redirect:/ruleName/list";
    }
}
