package com.ikubinfo.rental.exceptions;

public class CarAlreadyExistsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CarAlreadyExistsException(String errMessage) {
        super(errMessage);
    }
}
