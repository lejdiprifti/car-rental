package com.ikubinfo.rental.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CarRentalNotFoundException extends RuntimeException {
    public CarRentalNotFoundException(String errMessage) {
        super(errMessage);
    }
}
