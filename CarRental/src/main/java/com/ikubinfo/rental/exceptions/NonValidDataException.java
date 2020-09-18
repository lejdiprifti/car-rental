package com.ikubinfo.rental.exceptions;

public class NonValidDataException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NonValidDataException(String errMessage) {
        super(errMessage);
    }
}
