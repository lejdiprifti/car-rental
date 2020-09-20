package com.ikubinfo.rental.model;

import lombok.Data;

import java.util.List;

@Data
public class UserPage {

    private Long totalRecords;
    private List<UserModel> userList;
}
