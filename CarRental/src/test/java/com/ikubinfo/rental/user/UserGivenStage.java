package com.ikubinfo.rental.user;

import com.ikubinfo.rental.util.TokenCreator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

@JGivenStage
public class UserGivenStage extends Stage<UserGivenStage> {

    @Autowired
    private TokenCreator tokenCreator;

    public UserGivenStage user_is_logged_in_as_user() {
        tokenCreator.createUserToken();
        return self();
    }
}
