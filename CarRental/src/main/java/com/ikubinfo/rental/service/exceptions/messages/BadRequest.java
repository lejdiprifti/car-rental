package com.ikubinfo.rental.service.exceptions.messages;

public enum BadRequest {

    CAR_HAS_ACTIVE_RESERVATIONS("Cannot delete car because it has active reservations"), CAR_ALREADY_EXISTS("Car already exists"),
    USER_DATA_MISSING("User data are missing."), AVAILABILITY_REQUIRED("Availability is required."),
    BRAND_REQUIRED("Brand is required."), PHOTO_REQUIRED("Photo is required."), PHOTO_TO_BE_LESS_KB("Photo needs to be less than 100kb."),
    YEAR_MUST_BE_VALID("Year must contain a valid value."), PRICE_POSITIVE("Price must be positive."),
    PLATE_REQUIRED("Plate is required."), DIESEL_REQUIRED("Diesel is required."), NAME_REQUIRED("Name is required."),
    DESCRIPTION_REQUIRED("Description is required."), CATEGORY_ALREADY_EXISTS("Category already exists."),
    CATEGORY_CONTAINS_CARS("Category cannot be deleted because it contains cars."), EMAIL_SENDING_FAILED("Email sending failed."),
    CAR_RESERVED("Car is reserved during the specified dates."), INVALID_DATES("EndDate cannot be before StartDate."),
    UNAUTHORIZED("User is unauthorized to do this action."), USER_ALREADY_EXISTS("User already exists."),
    EMAIL_REQUIRED("Email is required."), MUST_BE_OVER_18("User must be over 18 years old."),
    PHONE_REQUIRED("Phone number is required."), ADDRESS_REQUIRED("Address is required."),
    BIRTHDATE_REQUIRED("Birthdate is required."), FIRSTNAME_REQUIRED("Firstname is required."),
    LASTNAME_REQUIRED("Lastname is required."), USERNAME_NOT_VALID("Username should be no less than 5 characters.");

    private String errorMessage;

    BadRequest(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
