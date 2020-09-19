package com.ikubinfo.rental.category;

import com.ikubinfo.rental.entity.CategoryEntity;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.integration.spring.JGivenStage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.assertEquals;

@JGivenStage
public class CategoryThenStage extends Stage<CategoryThenStage> {

    @PersistenceContext
    private EntityManager entityManager;

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
}
