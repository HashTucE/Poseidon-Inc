package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.services.CurveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurveControllerTest {



    @InjectMocks
    private CurveController curveController;

    @Mock
    private CurveService curveService;

    @Mock
    BindingResult result;

    @Mock
    private Model model;


    @Test
    void homeTest() throws EmptyListException {

        //given
        List<CurvePoint> curveList = Arrays.asList(new CurvePoint(), new CurvePoint());
        when(curveService.findAll()).thenReturn(curveList);

        //when
        String result = curveController.home(model);

        //then
        assertEquals("curvePoint/list", result);
        verify(model).addAttribute("curveList", curveList);
    }


    @Test
    void homeNegativeTest() throws EmptyListException {

        //given
        String alertMessage = "There is no curve yet.";
        doThrow(new EmptyListException("curve")).when(curveService).findAll();

        //when
        String result = curveController.home(model);

        //then
        assertEquals("curvePoint/list", result);
        verify(model).addAttribute("alertMessage", alertMessage);
    }


    @Test
    public void addCurveFormTest() {
        //given
        CurvePoint curvePoint = new CurvePoint();

        //when
        String result = curveController.addCurveForm(curvePoint);

        //then
        assertEquals("curvePoint/add", result);
    }


    @Test
    public void validateTest() {


        //given
        CurvePoint curvePoint = new CurvePoint();
        when(result.hasErrors()).thenReturn(false);

        //when
        String string = curveController.validate(curvePoint, result);

        //then
        assertEquals("redirect:/curvePoint/list", string);
        verify(curveService).addCurvePoint(curvePoint);
    }


    @Test
    public void validateNegativeTest() {


        //given
        CurvePoint curvePoint = new CurvePoint();
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = curveController.validate(curvePoint, result);

        //then
        assertEquals("curvePoint/add", string);
    }


    @Test
    public void showUpdateFormTest() throws NotExistingException {

        //given
        int id = 123;
        CurvePoint curvePoint = new CurvePoint();
        when(curveService.findById(id)).thenReturn(curvePoint);

        //when
        String result = curveController.showUpdateForm(id, model);

        //then
        assertEquals("curvePoint/update", result);
        verify(model).addAttribute("curvePoint", curvePoint);
    }


    @Test
    public void updateCurveTest() throws NotExistingException {

        //given
        CurvePoint curvePoint = new CurvePoint(1, 10.0, 10.0);
        curvePoint.setId(1);

        //when
        String string = curveController.updateCurve(1, curvePoint, result);

        //then
        assertEquals("redirect:/curvePoint/list", string);
        verify(curveService, times(1)).updateCurvePoint(1, curvePoint);
    }


    @Test
    public void updateCurveNegativeTest() throws NotExistingException {

        //given
        CurvePoint curvePoint = new CurvePoint(1, 10.0, 10.0);
        curvePoint.setId(1);
        when(result.hasErrors()).thenReturn(true);

        //when
        String string = curveController.updateCurve(1, curvePoint, result);

        //then
        assertEquals("curvePoint/update", string);
    }


    @Test
    public void deleteCurveTest() throws NotExistingException {

        //given//when
        String string = curveController.deleteCurve(1);

        //then
        assertEquals("redirect:/curvePoint/list", string);
        verify(curveService, times(1)).deleteById(1);
    }
}
