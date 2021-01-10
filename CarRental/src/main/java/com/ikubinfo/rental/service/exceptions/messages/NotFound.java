package com.ikubinfo.rental.service.exceptions.messages;

public enum NotFound {

    USER_NOT_FOUND("User not found"), CAR_NOT_FOUND("Car not found"), RESERVATION_NOT_FOUND("Reservation not found"),
    CATEGORY_NOT_FOUND("Category not found");
    private String errorMessage;

    NotFound(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
