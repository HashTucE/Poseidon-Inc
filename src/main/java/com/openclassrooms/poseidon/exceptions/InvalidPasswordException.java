package com.openclassrooms.poseidon.exceptions;

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException() {
        super(String.format("The password"));
    }
}
