package com.ikubinfo.rental.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ActiveReservationsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ActiveReservationsException(String message) {
        super(message);
    }

}
