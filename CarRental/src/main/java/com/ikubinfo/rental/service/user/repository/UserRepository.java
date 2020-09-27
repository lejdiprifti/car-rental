package com.ikubinfo.rental.service.user.repository;

import com.ikubinfo.rental.service.user.dto.UserEntity;
import com.ikubinfo.rental.service.user.dto.UserFilter;

import java.util.List;

public interface UserRepository {

    List<UserEntity> getAll(UserFilter userFilter);

    UserEntity getById(Long id);

    UserEntity getByUsername(String username);

    UserEntity getByUsernameAndPassword(String username, String password);

    Long countActiveUsers(String name);

    Long countActiveUsers();

    void save(UserEntity user);

    void edit(UserEntity user);

}
