package com.ikubinfo.rental.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class CarRentalUnauthorizedException extends RuntimeException {
    public CarRentalUnauthorizedException(String errMessage) {
        super(errMessage);
    }
}
