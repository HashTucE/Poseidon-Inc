package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.Rating;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class RatingController {


    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }



    @RequestMapping("/rating/list")
    public String home(Model model) {

        try {
            model.addAttribute("ratingList", ratingService.findAll());
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
        }
        return "rating/list";
    }



    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {

        return "rating/add";
    }



    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result) {

        if (!result.hasErrors()) {
            ratingService.addRating(rating);
            return "redirect:/rating/list";
        }
        return "rating/add";
    }



    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        Rating rating = ratingService.findById(id);
        model.addAttribute("rating", rating);

        return "rating/update";
    }



    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            ratingService.updateRating(id, rating);
            return "redirect:/rating/list";
        }
        return "rating/update";
    }



    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) throws NotExistingException {

        ratingService.deleteById(id);
        return "redirect:/rating/list";
    }
}
