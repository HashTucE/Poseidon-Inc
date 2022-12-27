package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.TradeService;
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
public class TradeController {


    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    private static final Logger log = LogManager.getLogger(TradeController.class);


    /**
     * display the trade list
     * @param model model
     * @return string path
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {

        try {
            model.addAttribute("tradeList", tradeService.findAll());
            log.info("display trade list page");
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
            log.info("display trade list page with empty list");
        }
        return "trade/list";
    }



    /**
     * display add trade form
     * @return string path
     */
    @GetMapping("/trade/add")
    public String addTradeForm(Trade trade) {

        log.info("display add trade page");
        return "trade/add";
    }



    /**
     * redirect after add trade
     * @param trade trade
     * @param result result
     * @return string path
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result) {

        if (!result.hasErrors()) {
            tradeService.addTrade(trade);
            log.info("validate trade");
            return "redirect:/trade/list";
        }
        log.error("can't validate trade");
        return "trade/add";
    }



    /**
     * display update trade form
     * @param id id
     * @param model model
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        Trade trade = tradeService.findById(id);
        model.addAttribute("trade", trade);

        log.info("display update trade page");
        return "trade/update";
    }



    /**
     * redirect after update trade
     * @param id id
     * @param trade trade
     * @param result result
     * @return string path
     * @throws NotExistingException e
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            tradeService.updateTrade(id, trade);
            log.info("update trade");
            return "redirect:/trade/list";
        }
        log.error("can't update trade");
        return "trade/update";
    }



    /**
     * redirect after delete trade
     * @param id id
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) throws NotExistingException {

        tradeService.deleteById(id);
        log.info("delete trade");
        return "redirect:/trade/list";
    }
}
