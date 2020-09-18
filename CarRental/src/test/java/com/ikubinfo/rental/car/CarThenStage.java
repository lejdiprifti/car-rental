package com.ikubinfo.rental.car;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.StatusEnum;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.assertEquals;

@JGivenStage
public class CarThenStage extends Stage<CarThenStage> {

    @PersistenceContext
    private EntityManager entityManager;


    public CarThenStage there_are_exactly_$_cars_with_status_$(int numberOfCars, String availability) {
        int actualNumberOfCars = getActiveCarsWithStatus(availability).size();
        assertEquals("The number of cars is", numberOfCars, actualNumberOfCars);
        return self();
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

    private List<CarEntity> getActiveCarsWithStatus(String availability) {
        TypedQuery<CarEntity> query = entityManager.createQuery("Select c from CarEntity c where c.active= true and c.availability= ?1", CarEntity.class);
        query.setParameter(1, StatusEnum.valueOf(availability));

        return query.getResultList();
    }
}
