package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.ikubinfo.rental.resource.utils.ApiConstants.*;

@RestController
@RequestMapping(path = REGISTER_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class RegisterResource {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserModel user) {
        userService.register(user);
    }
}
