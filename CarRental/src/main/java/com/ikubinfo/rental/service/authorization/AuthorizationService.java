package com.ikubinfo.rental.service.authorization;

public interface AuthorizationService {

    void isUserAuthorized();
    String getCurrentLoggedUserUsername();
}
