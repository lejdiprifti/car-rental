package com.ikubinfo.rental.car;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.model.enums.StatusEnum;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.CarRentalNotFoundException;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@JGivenStage
public class CarThenStage extends Stage<CarThenStage> {

    @PersistenceContext
    private EntityManager entityManager;

    @ExpectedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    @ExpectedScenarioState
    private CarRentalNotFoundException carRentalNotFoundException;

    public CarThenStage there_are_exactly_$_cars_with_status_$(int numberOfCars, String availability) {
        int actualNumberOfCars = getActiveCarsWithStatus(availability).size();
        assertEquals("The number of cars is", numberOfCars, actualNumberOfCars);
        return self();
    }

    private List<CarEntity> getActiveCarsWithStatus(String availability) {
        TypedQuery<CarEntity> query = entityManager.createQuery("Select c from CarEntity c where c.active= true and c.availability= ?1", CarEntity.class);
        query.setParameter(1, StatusEnum.valueOf(availability));

        return query.getResultList();
    }

    public CarThenStage there_are_exactly_$_cars(int numberOfCars) {
        int actualNumberOfCars = getActiveCars().size();
        assertEquals("The number of cars is", numberOfCars, actualNumberOfCars);
        return self();
    }

    private List<CarEntity> getActiveCars() {
        TypedQuery<CarEntity> query = entityManager.createQuery("Select c from CarEntity c where c.active = true", CarEntity.class);
        return query.getResultList();
    }

    public CarThenStage a_bad_request_exception_with_message_$_is_thrown(String errorMessage) {
        assertNotNull("CarRentalBadRequestException is not null", carRentalBadRequestException);
        assertEquals("Error message of CarRentalBadRequestException is", errorMessage, carRentalBadRequestException.getMessage());
        return self();
    }

    public CarThenStage a_not_found_exception_with_message_$_is_thrown(String errorMessage) {
        assertNotNull("CarRentalNotFoundException is not null", carRentalNotFoundException);
        assertEquals("Error message of CarRentalNotFoundException is", errorMessage, carRentalNotFoundException.getMessage());
        return self();
    }
}
