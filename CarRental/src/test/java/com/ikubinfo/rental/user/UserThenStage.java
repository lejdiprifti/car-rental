package com.ikubinfo.rental.user;

import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.entity.UserEntity;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.*;

@JGivenStage
public class UserThenStage extends Stage<UserThenStage> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ExpectedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    public UserThenStage there_are_exactly_$_users_with_username_$(int numberOfUsers, String username) {
        int actualNumberOfUsers = getUsersWithUsername(username).size();
        assertEquals("Number of users with the username specified is", numberOfUsers, actualNumberOfUsers);
        return self();
    }

    public void the_password_of_user_$_is_updated_to_$(String username, String updatedPassword) {
        assertTrue("Updated password is", passwordEncoder.matches(updatedPassword, getUsersWithUsername(username).get(0).getPassword()));
    }

    private List<UserEntity> getUsersWithUsername(String username) {
        TypedQuery<UserEntity> query = entityManager.createQuery("Select u from UserEntity u where u.username = ?1 and u.active = true", UserEntity.class);
        query.setParameter(1, username);
        return query.getResultList();
    }

    public UserThenStage there_are_exactly_$_reservations_of_user_$(int numberOfReservations, String username) {
        List<ReservationEntity> reservationEntities = getActiveReservationsByUser(username);
        assertEquals("Number of reservations is", numberOfReservations, reservationEntities.size());
        return self();
    }

    private List<ReservationEntity> getActiveReservationsByUser(String username) {
        TypedQuery<ReservationEntity> query = entityManager.createQuery("Select r from ReservationEntity r where r.active = true and r.user.username = ?1", ReservationEntity.class);
        query.setParameter(1, username);
        return query.getResultList();
    }

    public void a_bad_request_exception_with_message_$_is_thrown(String errorMessage) {
        assertNotNull("CarRentalBadRequestException is not null", carRentalBadRequestException);
        assertEquals("Error message of CarRentalBadRequestException is", errorMessage, carRentalBadRequestException.getMessage());
    }

    public UserThenStage the_lastname_of_user_$_is_updated_to_$(String username, String updatedLastname) {
        assertEquals("Last name is ", updatedLastname, getUsersWithUsername(username).get(0).getLastName());
        return self();
    }
}
