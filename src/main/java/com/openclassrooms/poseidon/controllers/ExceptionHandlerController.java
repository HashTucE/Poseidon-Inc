package com.openclassrooms.poseidon.controllers;

import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {


    /**
     * Handle NotExistingException to display the error message
     * @param ex exception
     * @return ResponseEntity with exception message
     */
    @ExceptionHandler(NotExistingException.class)
    public ResponseEntity<String> handleNotExistingException(NotExistingException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    /**
     * Handle EmptyListException to display the error message
     * @param ex exception
     * @return ResponseEntity with exception message
     */
    @ExceptionHandler(EmptyListException.class)
    public ResponseEntity<String> handleEmptyListException(EmptyListException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
