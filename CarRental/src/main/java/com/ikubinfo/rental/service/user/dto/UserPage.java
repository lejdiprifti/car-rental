package com.ikubinfo.rental.service.user.dto;

import com.ikubinfo.rental.service.user.dto.UserModel;
import lombok.Data;

import java.util.List;

@Data
public class UserPage {

    private Long totalRecords;
    private List<UserModel> userList;
}
