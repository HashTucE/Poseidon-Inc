package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.Trade;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.TradeService;
import jakarta.validation.Valid;
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



    @RequestMapping("/trade/list")
    public String home(Model model) {

        try {
            model.addAttribute("tradeList", tradeService.findAll());
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
        }
        return "trade/list";
    }



    @GetMapping("/trade/add")
    public String addTradeForm(Trade trade) {

        return "trade/add";
    }



    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result) {

        if (!result.hasErrors()) {
            tradeService.addTrade(trade);
            return "redirect:/trade/list";
        }
        return "trade/add";
    }



    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        Trade trade = tradeService.findById(id);
        model.addAttribute("trade", trade);

        return "trade/update";
    }



    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            tradeService.updateTrade(id, trade);
            return "redirect:/trade/list";
        }
        return "trade/update";
    }



    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) throws NotExistingException {

        tradeService.deleteById(id);
        return "redirect:/trade/list";
    }
}
