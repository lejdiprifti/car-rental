package com.ikubinfo.rental.security.service;

import com.ikubinfo.rental.converter.RoleConverter;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.security.model.LoginRequest;
import com.ikubinfo.rental.security.model.LoginResponse;
import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.security.jwt_configuration.JwtTokenUtil;
import com.ikubinfo.rental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CarRentalUserDetailsService implements UserDetailsService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleConverter roleConverter;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        UserDetails userDetails = loadUserByUsername(loginRequest.getUsername());
        UserModel model = userService.getByUsername(loginRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails, roleConverter.toEntity(model.getRole()));
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwt(token);
        loginResponse.setUser(model);
        return loginResponse;
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new CarRentalBadRequestException("User is disabled");
        } catch (BadCredentialsException e) {
            throw new CarRentalBadRequestException("Invalid Credentials");
        }
    }


    public UserDetails loadUserByUsername(String username) {
        UserModel model = userService.getByUsername(username);
        return new User(model.getUsername(), model.getPassword(), new ArrayList<>());
    }
}
