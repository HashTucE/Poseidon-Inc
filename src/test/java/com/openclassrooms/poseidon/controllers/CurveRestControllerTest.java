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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurveRestControllerTest {



    @InjectMocks
    private CurveRestController curveRestController;

    @Mock
    private CurveService curveService;



    @Test
    public void getAllCurveTest() throws EmptyListException {

        //given
        List<CurvePoint> expectedCurves = new ArrayList<>();
        expectedCurves.add(new CurvePoint(1, 1.0, 1.0));
        expectedCurves.add(new CurvePoint(1, 1.0, 1.0));
        when(curveService.findAll()).thenReturn(expectedCurves);

        //when
        ResponseEntity<List<CurvePoint>> response = curveRestController.getAllCurves();

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedCurves, response.getBody());
    }


    @Test
    public void getAllCurveNegativeTest() throws EmptyListException {

        //given
        when(curveService.findAll()).thenReturn(new ArrayList<>());

        //when
        ResponseEntity<List<CurvePoint>> response = curveRestController.getAllCurves();

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void getCurveTest() throws NotExistingException {

        //given
        CurvePoint expectedCurve = new CurvePoint(1, 1.0, 1.0);
        when(curveService.findById(1)).thenReturn(expectedCurve);

        //when
        ResponseEntity<CurvePoint> response = curveRestController.getCurve(1);

        //then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(expectedCurve, response.getBody());
    }


    @Test
    public void getCurveNegativeTest() throws NotExistingException {

        //given
        when(curveService.findById(1)).thenReturn(null);

        //when
        ResponseEntity<CurvePoint> response = curveRestController.getCurve(1);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void addCurveNegativeTest() {

        //given
        CurvePoint curve = new CurvePoint(null, 1.0, 1.0);

        //when
        ResponseEntity<String> response = curveRestController.addCurve(curve);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CurvePointId is mandatory", response.getBody());
    }


    @Test
    public void addCurveNegativeTest2() {

        //given
        CurvePoint curve = new CurvePoint(1, null, 1.0);

        //when
        ResponseEntity<String> response = curveRestController.addCurve(curve);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Term is mandatory", response.getBody());
    }


    @Test
    public void addCurveNegativeTest3() {

        //given
        CurvePoint curve = new CurvePoint(1, 1.0, null);

        //when
        ResponseEntity<String> response = curveRestController.addCurve(curve);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Value is mandatory", response.getBody());
    }


    @Test
    public void addCurveTest() {

        //given
        CurvePoint curve = new CurvePoint(1, 1.0, 1.0);

        //when
        ResponseEntity<String> response = curveRestController.addCurve(curve);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Curve created !", response.getBody());
    }


    @Test
    public void updateCurveTest() throws NotExistingException {

        //given
        CurvePoint curve = new CurvePoint(1, 1.0, 1.0);
        when(curveService.existsById(1)).thenReturn(true);

        //when
        ResponseEntity<String> response = curveRestController.updateCurve(1, curve);

        //then
        verify(curveService).updateCurvePoint(1, curve);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void updateCurveNegativeTest() throws NotExistingException {

        //given
        CurvePoint curve = new CurvePoint(1, 1.0, 1.0);
        when(curveService.existsById(1)).thenReturn(false);

        //when
        ResponseEntity<String> response = curveRestController.updateCurve(1, curve);

        //then
        verify(curveService, never()).updateCurvePoint(1, curve);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void deleteCurveTest() throws NotExistingException {

        // given
        int curveId = 1;
        CurvePoint curve = new CurvePoint();
        curve.setId(curveId);
        when(curveService.existsById(curveId)).thenReturn(true);

        // when
        ResponseEntity<String> response = curveRestController.deleteCurve(curveId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(curveService).existsById(curveId);
        verify(curveService).deleteById(curveId);
    }


    @Test
    public void deleteCurveNegativeTest() throws NotExistingException {

        // given
        int curveId = 1;
        when(curveService.existsById(curveId)).thenReturn(false);

        // when
        ResponseEntity<String> response = curveRestController.deleteCurve(curveId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(curveService).existsById(curveId);
    }
}
