package com.ikubinfo.rental.exceptions.messages;

public enum BadRequest {

    CAR_HAS_ACTIVE_RESERVATIONS("Cannot delete car because it has active reservations"), CAR_ALREADY_EXISTS("Car already exists"),
    USER_DATA_MISSING("User data are missing."), AVAILABILITY_REQUIRED("Availability is required."),
    BRAND_REQUIRED("Brand is required."), PHOTO_REQUIRED("Photo is required."), PHOTO_TO_BE_LESS_KB("Photo needs to be less than 100kb."),
    YEAR_MUST_BE_VALID("Year must contain a valid value."), PRICE_POSITIVE("Price must be positive."),
    PLATE_REQUIRED("Plate is required."), DIESEL_REQUIRED("Diesel is required."), NAME_REQUIRED("Name is required."),
    DESCRIPTION_REQUIRED("Description is required."), CATEGORY_ALREADY_EXISTS("Category already exists."),
    CATEGORY_CONTAINS_CARS("Category cannot be deleted because it contains cars."), EMAIL_SENDING_FAILED("Email sending failed."),
    CAR_RESERVED("Car is reserved during the specified dates."), INVALID_DATES("EndDate cannot be before StartDate.");

    private String errorMessage;

    BadRequest(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
