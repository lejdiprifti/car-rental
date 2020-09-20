package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.LoginRequest;
import com.ikubinfo.rental.model.LoginResponse;
import com.ikubinfo.rental.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/login", produces = "application/json")
@CrossOrigin("http://localhost:4200")
public class LoginResource {

    @Autowired
    private LoginService loginService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(loginService.authenticate(request), HttpStatus.OK);
    }
}
