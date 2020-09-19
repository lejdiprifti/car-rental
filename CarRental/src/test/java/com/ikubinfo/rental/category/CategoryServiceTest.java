package com.ikubinfo.rental.category;

import com.ikubinfo.rental.CarRentalTest;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
import com.ikubinfo.rental.exceptions.messages.NotFound;
import com.tngtech.jgiven.integration.spring.SpringScenarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.ikubinfo.rental.car.util.CarUtils.NON_EXISTING_CATEGORY_ID;

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
        given().admin_adds_new_category();
        when().admin_tries_to_update_category();
        then().there_are_exactly_$_categories_with_description_$(1, "some updated description");
    }

    @Test
    public void admin_deletes_category_successfully() {
        given().admin_adds_new_category();
        when().admin_tries_to_delete_category();
        then().there_are_exactly_$_categories(0);
    }

    @Test
    public void admin_cannot_add_category_with_missing_data() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_add_new_category_with_missing_data();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.USER_DATA_MISSING.getErrorMessage());
    }

    @Test
    public void admin_cannot_add_two_categories_with_the_same_name() {
        given().admin_adds_new_category();
        when().admin_tries_to_add_new_category();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CATEGORY_ALREADY_EXISTS.getErrorMessage());
    }

    @Test
    public void admin_cannot_update_non_existing_category() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_update_category_with_id_$(NON_EXISTING_CATEGORY_ID);
        then().a_not_found_exception_with_message_$_is_thrown(NotFound.CATEGORY_NOT_FOUND.getErrorMessage());
    }

    @Test
    public void admin_cannot_update_category_with_same_name_as_another_category() {
        given().admin_has_added_two_categories();
        when().admin_tries_to_update_category();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CATEGORY_ALREADY_EXISTS.getErrorMessage());
    }

    @Test
    public void admin_cannot_delete_non_existing_category() {
        given().user_is_logged_in_as_admin();
        when().admin_tries_to_delete_non_existing_category_with_id_$(NON_EXISTING_CATEGORY_ID);
        then().a_not_found_exception_with_message_$_is_thrown(NotFound.CATEGORY_NOT_FOUND.getErrorMessage());
    }

    @Test
    public void admin_cannot_delete_category_if_it_contains_cars() {
        given().category_has_cars();
        when().admin_tries_to_delete_category();
        then().a_bad_request_exception_with_message_$_is_thrown(BadRequest.CATEGORY_CONTAINS_CARS.getErrorMessage());
    }

    @Test
    public void exception_is_thrown_when_trying_to_get_non_existing_category() {
        given().user_is_logged_in_as_user();
        when().admin_tries_to_get_category_by_id(NON_EXISTING_CATEGORY_ID);
        then().a_not_found_exception_with_message_$_is_thrown(NotFound.CATEGORY_NOT_FOUND.getErrorMessage());
    }
}
