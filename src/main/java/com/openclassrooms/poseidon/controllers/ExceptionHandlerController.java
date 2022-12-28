package com.openclassrooms.poseidon.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

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


    /**
     * Handle HttpMessageNotReadableException to display the error message
     * @param ex exception
     * @return ResponseEntity with exception message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {


        JsonMappingException mappingException = (JsonMappingException) ex.getCause();
        List<JsonMappingException.Reference> path = mappingException.getPath();

        if (path.isEmpty()) {
            return new ResponseEntity<>("Deserialization error JSON", HttpStatus.BAD_REQUEST);
        }
        String fieldName = path.get(0).getFieldName();
        return new ResponseEntity<>("Field " + fieldName + " should contain only numbers", HttpStatus.BAD_REQUEST);
    }
}
