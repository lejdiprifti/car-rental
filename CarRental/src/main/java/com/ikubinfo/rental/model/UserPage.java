package com.ikubinfo.rental.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPage {

	private Long totalRecords;
	private List<UserModel> userList;
}
