package com.openclassrooms.poseidon.exceptions;

public class NotExistingException extends Exception {

    public NotExistingException(String obj, int id) {
        super(String.format("%s with id number %d does not exist !", obj, id));
    }
}
