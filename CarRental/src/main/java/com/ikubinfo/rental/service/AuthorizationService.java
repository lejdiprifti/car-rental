package com.ikubinfo.rental.service;

import com.ikubinfo.rental.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthorizationService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public boolean isUserAuthorized() {
        if (((Integer) jwtTokenUtil.getRole().get("id")).intValue() == 1) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized.");
        }
    }
}
