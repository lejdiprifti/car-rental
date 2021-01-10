package com.ikubinfo.rental.security.api;

import com.ikubinfo.rental.security.model.LoginRequest;
import com.ikubinfo.rental.security.model.LoginResponse;
import com.ikubinfo.rental.security.service.CarRentalUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ikubinfo.rental.controller.constants.ApiConstants.CLIENT_APP;
import static com.ikubinfo.rental.controller.constants.ApiConstants.LOGIN_PATH;

@RestController
@RequestMapping(path = LOGIN_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class LoginController {

    @Autowired
    private CarRentalUserDetailsService carRentalUserDetailsService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(carRentalUserDetailsService.authenticate(request), HttpStatus.OK);
    }
}
