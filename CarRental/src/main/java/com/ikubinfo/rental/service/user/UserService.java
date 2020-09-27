package com.ikubinfo.rental.service.user;

import com.ikubinfo.rental.service.user.dto.UserFilter;
import com.ikubinfo.rental.service.user.dto.UserModel;
import com.ikubinfo.rental.service.user.dto.UserPage;

public interface UserService {

    UserModel getById(Long id);

    UserPage getAll(UserFilter userFilter);

    UserModel getByUsername(String username);

    void register(UserModel user);

    void editProfile(UserModel user);

    void editPassword(UserModel user);

    void delete();
}
