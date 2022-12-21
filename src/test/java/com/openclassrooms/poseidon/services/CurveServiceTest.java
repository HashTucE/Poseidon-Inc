package com.openclassrooms.poseidon.services;

import com.openclassrooms.poseidon.domain.CurvePoint;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurveServiceTest {


    @InjectMocks
    private CurveService curveService;

    @Mock
    private CurvePointRepository curveRepository;

    private final CurvePoint curve = new CurvePoint(1, 1.0, 1.0);



    @Test
    void findAllTest() throws EmptyListException {

        //given
        List<CurvePoint> curvePoints = Arrays.asList(new CurvePoint(), new CurvePoint(), new CurvePoint());
        when(curveRepository.findAll()).thenReturn(curvePoints);

        //when
        List<CurvePoint> result = curveService.findAll();

        //then
        assertEquals(curvePoints, result);
    }


    @Test
    void findAllNegativeTest() {

        //given
        when(curveRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        try {
            curveService.findAll();
        } catch (EmptyListException e) {

            //then
            assertEquals("There is no curve yet.", e.getMessage());
        }
    }


    @Test
    public void existsByIdTest() throws NotExistingException {

        //given
        int tradeId = 1;
        when(curveRepository.existsById(tradeId)).thenReturn(true);

        //when
        boolean exists = curveService.existsById(tradeId);

        //then
        assertTrue(exists);
        verify(curveRepository, times(1)).existsById(tradeId);
    }


    @Test
    public void existsByIdNegativeTest() {

        //given
        int id = 1;
        when(curveRepository.existsById(id)).thenReturn(false);

        //when
        try {
            curveService.existsById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("curve with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void findByIdTest() throws NotExistingException {


        //given
        int id = 1;
        when(curveRepository.findById(id)).thenReturn(Optional.of(curve));

        // when
        curveService.findById(id);

        //then
        verify(curveRepository, times(1)).findById(1);
        assertEquals(curve, curveService.findById(id));
    }


    @Test
    void findByIdNegativeTest() {


        //given
        int id = 1;
        when(curveRepository.findById(id)).thenReturn(Optional.empty());

        //when
        try {
            curveService.findById(id);
        } catch (NotExistingException e) {
            //then
            assertEquals("curve with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void addCurveTest() {

        //given//when
        curveService.addCurvePoint(curve);

        //then
        verify(curveRepository, times(1)).save(any());
    }


    @Test
    void updateCurveTest() throws NotExistingException {

        //given
        CurvePoint optionalCurve = new CurvePoint(1, 1.0, 1.0);

        //when
        when(curveRepository.findById(1)).thenReturn(Optional.of(curve));
        curveService.updateCurvePoint(1,optionalCurve);

        //then
        verify(curveRepository, times(1)).save(any());
    }


    @Test
    void updateCurveNegativeTest() {

        //given
        when(curveRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            curveService.updateCurvePoint(1, curve);
        } catch (NotExistingException e) {
            //then
            assertEquals("curve with id number 1 does not exist !", e.getMessage());
        }
    }


    @Test
    void deleteByIdTest() throws NotExistingException {

        //given//when
        when(curveRepository.findById(1)).thenReturn(Optional.of(curve));
        curveService.deleteById(1);

        //then
        verify(curveRepository, times(1)).delete(any());
    }


    @Test
    void deleteByIdNegativeTest() {

        //given
        when(curveRepository.findById(1)).thenReturn(Optional.empty());

        //when
        try {
            curveService.deleteById(1);
        } catch (NotExistingException e) {
            //then
            assertEquals("curve with id number 1 does not exist !", e.getMessage());
        }
    }
}
