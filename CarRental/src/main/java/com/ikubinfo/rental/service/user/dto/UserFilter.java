package com.ikubinfo.rental.service.user.dto;

import lombok.Data;

@Data
public class UserFilter {
    private int startIndex;
    private int pageSize;
    private String name = "";
}
