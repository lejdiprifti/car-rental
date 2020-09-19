package com.ikubinfo.rental.reservation;

import com.ikubinfo.rental.category.CategoryThenStage;
import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@JGivenStage
public class ReservationThenStage extends Stage<ReservationThenStage> {

    @PersistenceContext
    private EntityManager entityManager;

    @ExpectedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    @ExpectedScenarioState
    private CarRentalNotFoundException carRentalNotFoundException;

    public ReservationThenStage there_are_exactly_$_reservations_of_user_$(int numberOfReservations, String username) {
        int actualNumberOfReservations = getActiveReservationsByUser(username).size();
        assertEquals("Number of reservations is", numberOfReservations, actualNumberOfReservations);
        return self();
    }

    private List<ReservationEntity> getActiveReservationsByUser(String username) {
        TypedQuery<ReservationEntity> query = entityManager.createQuery("Select r from ReservationEntity r where r.active = true and r.user.username = ?1", ReservationEntity.class);
        query.setParameter(1, username);
        return query.getResultList();
    }

    public ReservationThenStage there_are_exactly_$_reservations_with_start_date_$(int numberOfReservations, String startDate) {
        int actualNumberOfReservations = getActiveReservationsByStartDate(startDate).size();
        assertEquals("Number of reservations is", numberOfReservations, actualNumberOfReservations);
        return self();
    }

    private List<ReservationEntity> getActiveReservationsByStartDate(String startDate) {
        TypedQuery<ReservationEntity> query = entityManager.createQuery("Select r from ReservationEntity r where r.active = true and r.startDate = ?1", ReservationEntity.class);
        query.setParameter(1, LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return query.getResultList();
    }

    public ReservationThenStage a_bad_request_exception_with_message_$_is_thrown(String errorMessage) {
        assertNotNull("CarRentalBadRequestException is not null", carRentalBadRequestException);
        assertEquals("Error message of CarRentalBadRequestException is", errorMessage, carRentalBadRequestException.getMessage());
        return self();
    }

    public ReservationThenStage a_not_found_exception_with_message_$_is_thrown(String errorMessage) {
        assertNotNull("CarRentalNotFoundException is not null", carRentalNotFoundException);
        assertEquals("Error message of CarRentalNotFoundException is", errorMessage, carRentalNotFoundException.getMessage());
        return self();
    }
}
