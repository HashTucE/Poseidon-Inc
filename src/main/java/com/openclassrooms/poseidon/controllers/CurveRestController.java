package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.constants.Log;
import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.CurveService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CurveRestController {



    private static final Logger log = LogManager.getLogger(CurveRestController.class);



    private final CurveService curveService;

    public CurveRestController(CurveService curveService) {
        this.curveService = curveService;
    }


    //GET, POST, PUT, DELETE CONTROLLERS//



    /**
     * Find all curves
     * @return CurvePoint with HTTP code 200 OK
     */
    @GetMapping("/curve/all")
    public ResponseEntity<List<CurvePoint>> getAllCurves() throws EmptyListException {
        List<CurvePoint> curves = curveService.findAll();
        if (curves.isEmpty()) {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(curves, HttpStatus.FOUND);
        }
    }


    /**
     * Find a curve by id
     * @param id of the curve
     * @return curve with HTTP code 200 found
     */
    @GetMapping("/curve")
    public ResponseEntity<CurvePoint> getCurve(@RequestParam int id) throws NotExistingException {
        CurvePoint curve = curveService.findById(id);
        if (curve != null) {
            log.info(Log.OBJECT_FOUND);
            return new ResponseEntity<>(curve, HttpStatus.FOUND);
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Create a curve
     * @param curve curve
     * @return curve with HTTP code 201 created
     */
    @PostMapping("/curve")
    public ResponseEntity<String> addCurve(@RequestBody CurvePoint curve) {

        if (curve.getCurveId() == null) {
            return new ResponseEntity<>("CurvePointId is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (curve.getTerm() == null) {
            return new ResponseEntity<>("Term is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (curve.getValue() == null) {
            return new ResponseEntity<>("Value is mandatory", HttpStatus.BAD_REQUEST);
        }
        curveService.addCurvePoint(curve);
        log.info(Log.OBJECT_CREATED);
        return new ResponseEntity<>("Curve created !", HttpStatus.CREATED);
    }


    /**
     * Update a curve
     * @param id of the curve and curve object
     * @return HTTP code 200 OK with confirmation string
     */
    @PutMapping("/curve")
    public ResponseEntity<String> updateCurve(@RequestParam int id, @RequestBody CurvePoint curve) throws NotExistingException {

        if(curveService.existsById(id)) {
            curveService.updateCurvePoint(id, curve);
            log.info(Log.OBJECT_MODIFIED);
            return ResponseEntity.ok().body("Curve with id " + id + " updated !");
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete a curve
     * @param id of the curve
     * @return HTTP code 200 OK with confirmation string
     */
    @DeleteMapping("/curve")
    public ResponseEntity<String> deleteCurve(@RequestParam int id) throws NotExistingException {

        if(curveService.existsById(id)) {
            curveService.deleteById(id);
            log.info(Log.OBJECT_DELETED);
            return ResponseEntity.ok().body("Curve with id " + id + " deleted !");
        } else {
            log.info(Log.OBJECT_NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}