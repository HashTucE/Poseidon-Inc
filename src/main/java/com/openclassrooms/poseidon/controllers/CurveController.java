package com.openclassrooms.poseidon.controllers;


import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.CurveService;
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
public class CurveController {


    private final CurveService curveService;

    public CurveController(CurveService curveService) {
        this.curveService = curveService;
    }

    private static final Logger log = LogManager.getLogger(CurveController.class);


    /**
     * display the curvePoint list
     * @param model model
     * @return string path
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {

        try {
            model.addAttribute("curveList", curveService.findAll());
            log.info("display curve list page");
        } catch (EmptyListException e) {
            model.addAttribute("alertMessage", e.getMessage());
            log.info("display curve list page with empty list");
        }
        return "curvePoint/list";
    }



    /**
     * display add curvePoint form
     * @return string path
     */
    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePoint bid) {

        log.info("display add curve page");
        return "curvePoint/add";
    }



    /**
     * redirect after add curvePoint
     * @param curvePoint curvePoint
     * @param result result
     * @return string path
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {

        if (!result.hasErrors()) {
            curveService.addCurvePoint(curvePoint);
            log.info("validate curve");
            return "redirect:/curvePoint/list";
        }
        log.error("can't validate curve");
        return "curvePoint/add";
    }



    /**
     * display update curvePoint form
     * @param id id
     * @param model model
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotExistingException {

        CurvePoint curvePoint = curveService.findById(id);
        model.addAttribute("curvePoint", curvePoint);

        log.info("display update curve page");
        return "curvePoint/update";
    }



    /**
     * redirect after update curvePoint
     * @param id id
     * @param curvePoint curvePoint
     * @param result result
     * @return string path
     * @throws NotExistingException e
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurve(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result) throws NotExistingException {

        if (!result.hasErrors()) {
            curveService.updateCurvePoint(id, curvePoint);
            log.info("update curve");
            return "redirect:/curvePoint/list";
        }
        log.error("can't update curve");
        return "curvePoint/update";
    }


    /**
     * redirect after delete curvePoint
     * @param id id
     * @return string path
     * @throws NotExistingException e
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id) throws NotExistingException {

        curveService.deleteById(id);
        log.info("delete curve");
        return "redirect:/curvePoint/list";
    }
}
