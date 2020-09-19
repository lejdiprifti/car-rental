package com.ikubinfo.rental.category;

import com.ikubinfo.rental.CarRentalTest;
import com.tngtech.jgiven.integration.spring.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@CarRentalTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryServiceTest extends SpringScenarioTest<CategoryGivenStage, CategoryWhenStage, CategoryThenStage> {

    @Test
    public void admin_adds_new_category_successfully() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_add_new_category();
        then().there_are_exactly_$_categories(1);
    }

    @Test
    public void admin_updates_category_successfully() {
        given().user_is_logged_in_as_admin()
                .and()
                .admin_adds_new_category();
        when().admin_tries_to_update_category();
        then().there_are_exactly_$_categories_with_description_$(1, "some updated description");
    }

    @Test
    public void admin_deletes_category_successfully() {
        given().user_is_logged_in_as_admin()
                .and()
                .admin_adds_new_category();
        when().admin_tries_to_delete_category();
        then().there_are_exactly_$_categories(0);
    }
}
