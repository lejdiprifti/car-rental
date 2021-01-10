package com.ikubinfo.rental.model.page;

import com.ikubinfo.rental.model.UserModel;
import lombok.Data;

import java.util.List;

@Data
public class UserPage {

    private Long totalRecords;
    private List<UserModel> userList;
}
