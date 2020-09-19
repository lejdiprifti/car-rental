package com.ikubinfo.rental.user;

import com.ikubinfo.rental.entity.UserEntity;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.integration.spring.JGivenStage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.assertEquals;

@JGivenStage
public class UserThenStage extends Stage<UserThenStage> {

    @PersistenceContext
    private EntityManager entityManager;

    public UserThenStage there_are_exactly_$_users_with_username_$(int numberOfUsers, String username) {
        int actualNumberOfUsers = getUsersWithUsername(username).size();
        assertEquals("Number of users with the username specified is", numberOfUsers, actualNumberOfUsers);
        return self();
    }

    private List<UserEntity> getUsersWithUsername(String username) {
        TypedQuery<UserEntity> query = entityManager.createQuery("Select u from UserEntity u where u.username = ?1 and u.active = true", UserEntity.class);
        query.setParameter(1, username);
        return query.getResultList();
    }
}
