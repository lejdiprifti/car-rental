package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.model.UserPage;
import com.ikubinfo.rental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user", produces = "application/json")
@CrossOrigin("http://localhost:4200")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserPage> getAll(@RequestParam("startIndex") int startIndex,
                                           @RequestParam("pageSize") int pageSize,
                                           @RequestParam(name = "name", required = false) String name) {
        return new ResponseEntity<UserPage>(userService.getAll(startIndex, pageSize, name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<UserModel>(userService.getById(id), HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@RequestBody UserModel user) {
        userService.edit(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        userService.delete();
    }
}
