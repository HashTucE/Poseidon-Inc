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


    /**
     * display the bidList
     * @param model model
     * @return string path
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {

        try {
            model.addAttribute("bidList", bidListService.findAll());
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
        }
        return "bidList/list";
    }



    /**
     * display add bid form
     * @return string path
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {

        return "bidList/add";
    }


    /**
     * redirect after add bid
     * @param bid bid
     * @param result result
     * @return string path
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result) {

        if (!result.hasErrors()) {
            bidListService.addBid(bid);
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }


    /**
     * display update bid form
     * @param id id
     * @param model model
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        BidList bid = bidListService.findById(id);
        model.addAttribute("bid", bid);

        return "bidList/update";
    }


    /**
     * redirect after update bid
     * @param id id
     * @param bid bid
     * @param result result
     * @return string path
     * @throws NotExistingException e
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bid,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            bidListService.updateBid(id, bid);
        return "redirect:/bidList/list";
        }
        return "bidList/update";
    }


    /**
     * redirect after delete bid
     * @param id id
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) throws NotExistingException {

        bidListService.deleteById(id);
        return "redirect:/bidList/list";
    }
}
