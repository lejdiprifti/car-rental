package com.ikubinfo.rental.controller;

import com.ikubinfo.rental.service.user.UserService;
import com.ikubinfo.rental.service.user.dto.UserModel;
import com.ikubinfo.rental.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.ikubinfo.rental.controller.constants.ApiConstants.*;

@RestController
@RequestMapping(path = REGISTER_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserModel user) {
        userService.register(user);
    }
}
