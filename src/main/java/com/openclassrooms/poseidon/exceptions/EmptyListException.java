package com.openclassrooms.poseidon.exceptions;


public class EmptyListException extends Exception {

    public EmptyListException(String obj) {
        super(String.format("There is no %s yet.", obj));
    }
}
