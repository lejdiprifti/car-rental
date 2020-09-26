package com.ikubinfo.rental.controller;

import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.model.page.UserPage;
import com.ikubinfo.rental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ikubinfo.rental.controller.constants.ApiConstants.CLIENT_APP;
import static com.ikubinfo.rental.controller.constants.ApiConstants.USER_PATH;

@RestController
@RequestMapping(path = USER_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserPage> getAll(@RequestParam("startIndex") int startIndex,
                                           @RequestParam("pageSize") int pageSize,
                                           @RequestParam(name = "name", defaultValue = "") String name) {
        return new ResponseEntity<>(userService.getAll(startIndex, pageSize, name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @PutMapping(path = "/update/profile", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editProfile(@RequestBody UserModel user) {
        userService.editProfile(user);
    }

    @PutMapping(path = "/update/password", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editPassword(@RequestBody UserModel user) {
        userService.editPassword(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        userService.delete();
    }
}
