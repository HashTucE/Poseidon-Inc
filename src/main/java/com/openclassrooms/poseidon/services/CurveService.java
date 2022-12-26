package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.CurvePointRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurveService {


    private static final Logger log = LogManager.getLogger(CurveService.class);

    @Autowired
    private CurvePointRepository curvePointRepository;


    /**
     * call findAll from repository
     * @return curve list
     * @throws EmptyListException e
     */
    public List<CurvePoint> findAll() throws EmptyListException {

        if(curvePointRepository.findAll().isEmpty()) {
            log.error("findAll Curve return an empty list");
            throw new EmptyListException("curve");
        }
        log.debug("findAll CurvePoint from service called");
        return curvePointRepository.findAll();
    }


    /**
     * check if an object exist by id
     * @param id id
     * @return boolean
     * @throws NotExistingException e
     */
    public boolean existsById(int id) throws NotExistingException {

        boolean isCurveExist = curvePointRepository.existsById(id);
        if (!isCurveExist) {
            throw new NotExistingException("curve", id);
        }
        log.debug("existsById = " + id + " from Curve service called");
        return true;
    }


    /**
     * call findById from repository
     * @param id id
     * @return curve object
     * @throws NotExistingException e
     */
    public CurvePoint findById(int id) throws NotExistingException {

        log.debug("findById = " + id + " from Curve service called");
        return curvePointRepository.findById(id).orElseThrow(()-> new NotExistingException("curve", id));
    }


    /**
     * call save method from repository
     * @param curvePoint curve
     * @return curve object
     */
    public CurvePoint addCurvePoint(CurvePoint curvePoint) {

        curvePointRepository.save(curvePoint);
        log.debug("addCurvePoint with id " + curvePoint.getId() + " from service called");
        return curvePoint;
    }


    /**
     * update a curve calling save from repository
     * @param id id
     * @param curvePoint curve
     * @throws NotExistingException e
     */
    public void updateCurvePoint(int id, CurvePoint curvePoint) throws NotExistingException {

        CurvePoint curvePointToModify = findById(id);

            curvePointToModify.setCurveId(curvePoint.getCurveId());
            curvePointToModify.setTerm(curvePoint.getTerm());
            curvePointToModify.setValue(curvePoint.getValue());
            curvePointRepository.save(curvePointToModify);
            log.debug("updateCurvePoint with id " + curvePoint.getCurveId() + " from service called with success");
    }


    /**
     * delete an object by id calling delete from repository
     * @param id id
     * @throws NotExistingException e
     */
    public void deleteById(int id) throws NotExistingException {

        CurvePoint optionalCurvePoint = findById(id);

            curvePointRepository.delete(optionalCurvePoint);
            log.debug("deleteById = " + id + " from CurvePoint service called with success");
    }
}
