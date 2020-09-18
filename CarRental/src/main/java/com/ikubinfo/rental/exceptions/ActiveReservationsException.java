package com.ikubinfo.rental.exceptions;

public class ActiveReservationsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ActiveReservationsException(String message) {
        super(message);
    }

}
