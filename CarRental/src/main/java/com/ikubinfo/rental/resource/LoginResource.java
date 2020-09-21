package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.LoginRequest;
import com.ikubinfo.rental.model.LoginResponse;
import com.ikubinfo.rental.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ikubinfo.rental.resource.constants.ApiConstants.*;

@RestController
@RequestMapping(path = LOGIN_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class LoginResource {

    @Autowired
    private LoginService loginService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(loginService.authenticate(request), HttpStatus.OK);
    }
}
