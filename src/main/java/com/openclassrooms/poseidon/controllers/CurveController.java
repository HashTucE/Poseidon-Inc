package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.CurveService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class CurveController {


    private final CurveService curveService;

    public CurveController(CurveService curveService) {
        this.curveService = curveService;
    }



    @RequestMapping("/curvePoint/list")
    public String home(Model model) {

        try {
            model.addAttribute("curveList", curveService.findAll());
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
        }
        return "curvePoint/list";
    }



    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePoint bid) {

        return "curvePoint/add";
    }



    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {

        if (!result.hasErrors()) {
            curveService.addCurvePoint(curvePoint);
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }



    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        CurvePoint curvePoint = curveService.findById(id);
        model.addAttribute("curvePoint", curvePoint);

        return "curvePoint/update";
    }



    @PostMapping("/curvePoint/update/{id}")
    public String updateCurve(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            curveService.updateCurvePoint(id, curvePoint);
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/update";
    }



    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id) throws NotExistingException {

        curveService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
