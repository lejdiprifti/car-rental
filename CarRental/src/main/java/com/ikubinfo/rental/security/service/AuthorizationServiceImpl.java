package com.ikubinfo.rental.security.service;

import com.ikubinfo.rental.security.jwt_configuration.JwtTokenUtil;
import com.ikubinfo.rental.service.authorization.AuthorizationService;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.messages.BadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void isUserAuthorized() {
        if ((Integer) jwtTokenUtil.getRole().get("id") != 1) {
            LOGGER.debug("User is not an admin and cannot complete the action required.");
            throw new CarRentalBadRequestException(BadRequest.UNAUTHORIZED.getErrorMessage());
        }
        LOGGER.debug("User is an admin and authorized to completed the action initiated.");
    }

    public String getCurrentLoggedUserUsername() {
        return jwtTokenUtil.getUsername();
    }
}
