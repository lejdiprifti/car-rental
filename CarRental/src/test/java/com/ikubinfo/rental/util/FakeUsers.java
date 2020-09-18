package com.ikubinfo.rental.util;

public enum FakeUsers {
    ADMIN("admintester", "Admin123_"), USER("usertester", "Admin123_");

    public String username;
    public String password;

    FakeUsers(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
