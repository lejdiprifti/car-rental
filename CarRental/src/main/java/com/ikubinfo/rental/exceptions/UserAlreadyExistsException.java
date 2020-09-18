package com.ikubinfo.rental.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String errMessage) {
        super(errMessage);
    }
}
