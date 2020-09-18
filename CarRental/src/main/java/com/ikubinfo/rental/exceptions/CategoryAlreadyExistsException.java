package com.ikubinfo.rental.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CategoryAlreadyExistsException(String errMessage) {
        super(errMessage);
    }
}
