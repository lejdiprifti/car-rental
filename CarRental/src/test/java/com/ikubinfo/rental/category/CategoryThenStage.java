package com.ikubinfo.rental.category;

import com.ikubinfo.rental.entity.CategoryEntity;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
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
public class CategoryThenStage extends Stage<CategoryThenStage> {

    @PersistenceContext
    private EntityManager entityManager;

    @ExpectedScenarioState
    private CarRentalBadRequestException carRentalBadRequestException;

    @ExpectedScenarioState
    private CarRentalNotFoundException carRentalNotFoundException;

    public CategoryThenStage there_are_exactly_$_categories(int numberOfCategories) {
        int actualNumberOfCategories = getActiveCategories().size();
        assertEquals("The number of categories is", numberOfCategories, actualNumberOfCategories);
        return self();
    }

    public CategoryThenStage there_are_exactly_$_categories_with_description_$(int numberOfCategories, String description) {
        int actualNumberOfCategories = getActiveCategoriesWithDescription(description).size();
        assertEquals("The number of categories is", numberOfCategories, actualNumberOfCategories);
        return self();
    }

    private List<CategoryEntity> getActiveCategories() {
        TypedQuery<CategoryEntity> query = entityManager.createQuery("Select c from CategoryEntity c where c.active = true", CategoryEntity.class);
        return query.getResultList();
    }

    private List<CategoryEntity> getActiveCategoriesWithDescription(String description) {
        TypedQuery<CategoryEntity> query = entityManager.createQuery("Select c from CategoryEntity c where c.active = true and c.description = ?1", CategoryEntity.class);
        query.setParameter(1, description);
        return query.getResultList();
    }

    public CategoryThenStage a_bad_request_exception_with_message_$_is_thrown(String errorMessage) {
        assertNotNull("CarRentalBadRequestException is not null", carRentalBadRequestException);
        assertEquals("Error message of CarRentalBadRequestException is", errorMessage, carRentalBadRequestException.getMessage());
        return self();
    }

    public CategoryThenStage a_not_found_exception_with_message_$_is_thrown(String errorMessage) {
        assertNotNull("CarRentalNotFoundException is not null", carRentalNotFoundException);
        assertEquals("Error message of CarRentalNotFoundException is", errorMessage, carRentalNotFoundException.getMessage());
        return self();
    }
}
