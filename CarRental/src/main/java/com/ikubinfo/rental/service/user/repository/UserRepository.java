package com.ikubinfo.rental.service.user.repository;

import com.ikubinfo.rental.service.user.dto.UserEntity;

import java.util.List;

public interface UserRepository {

    List<UserEntity> getAll(int startIndex, int pageSize, String name);

    UserEntity getById(Long id);

    UserEntity getByUsername(String username);

    UserEntity getByUsernameAndPassword(String username, String password);

    Long countActiveUsers(String name);

    Long countActiveUsers();

    void save(UserEntity user);

    void edit(UserEntity user);

}
