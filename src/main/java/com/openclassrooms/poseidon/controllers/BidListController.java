package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.BidList;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.BidListService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class BidListController {


    private final BidListService bidListService;

    public BidListController(BidListService bidListService) {
        this.bidListService = bidListService;
    }



    @RequestMapping("/bidList/list")
    public String home(Model model) {

        try {
            model.addAttribute("bidList", bidListService.findAll());
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
        }
        return "bidList/list";
    }



    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {

        return "bidList/add";
    }



    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result) {

        if (!result.hasErrors()) {
            bidListService.addBid(bid);
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }



    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        BidList bid = bidListService.findById(id);
        model.addAttribute("bid", bid);

        return "bidList/update";
    }



    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bid,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            bidListService.updateBid(id, bid);
        return "redirect:/bidList/list";
        }
        return "bidList/update";
    }



    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) throws NotExistingException {

        bidListService.deleteById(id);
        return "redirect:/bidList/list";
    }
}
