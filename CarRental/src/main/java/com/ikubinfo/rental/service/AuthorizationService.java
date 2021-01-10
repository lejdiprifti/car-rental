package com.ikubinfo.rental.service;

import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.messages.BadRequest;
import com.ikubinfo.rental.security.jwt_configuration.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void isUserAuthorized() {
        if (((Integer) jwtTokenUtil.getRole().get("id")).intValue() != 1) {
            LOGGER.debug("User is not an admin and cannot complete the action required.");
            throw new CarRentalBadRequestException(BadRequest.UNAUTHORIZED.getErrorMessage());
        }
        LOGGER.debug("User is an admin and authorized to completed the action initiated.");
    }
}
