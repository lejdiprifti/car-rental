package com.ikubinfo.rental.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CarRentalBadRequestException extends RuntimeException {
    public CarRentalBadRequestException(String errMessage) {
        super(errMessage);
    }
}
