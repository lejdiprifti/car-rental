package com.ikubinfo.rental.service.user.converter;

import com.ikubinfo.rental.service.user.dto.UserEntity;
import com.ikubinfo.rental.service.user.dto.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper modelMapper;

    public UserModel toModel(UserEntity entity) {
        return modelMapper.map(entity, UserModel.class);
    }

    public UserEntity toEntity(UserModel model) {
        return modelMapper.map(model, UserEntity.class);
    }

    public List<UserModel> toModel(List<UserEntity> entityList) {
        List<UserModel> modelList = new ArrayList<>();
        for (UserEntity entity : entityList) {
            modelList.add(toModel(entity));
        }
        return modelList;
    }
}
