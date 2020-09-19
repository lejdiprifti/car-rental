package com.ikubinfo.rental.exceptions.messages;

public enum Unauthorized {

    USER_UNAUTHORIZED("User is not authorized.");

    private String errorMessage;
    Unauthorized(String errorMessage) {this.errorMessage = errorMessage;}

    public String getErrorMessage() {return errorMessage;}
}
