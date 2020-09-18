package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/register", produces = "application/json")
@CrossOrigin("http://localhost:4200")
public class RegisterResource {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserModel user) {
        userService.register(user);
    }
}
