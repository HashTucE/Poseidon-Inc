package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerTest {


    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;



    @Test
    public void handleNotExistingExceptionTest() {
        NotExistingException ex = new NotExistingException("test", 1);

        ResponseEntity<String> response = exceptionHandlerController.handleNotExistingException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("test with id number 1 does not exist !", response.getBody());
    }


    @Test
    public void handleEmptyListExceptionTest() {
        EmptyListException ex = new EmptyListException("test");

        ResponseEntity<String> response = exceptionHandlerController.handleEmptyListException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There is no test yet.", response.getBody());
    }


}
