package com.ikubinfo.rental.converter;

import com.ikubinfo.rental.entity.RoleEntity;
import com.ikubinfo.rental.model.RoleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {

    @Autowired
    private ModelMapper modelMapper;

    public RoleModel toModel(RoleEntity entity) {
        return modelMapper.map(entity, RoleModel.class);
    }

    public RoleEntity toEntity(RoleModel model) {
        return modelMapper.map(model, RoleEntity.class);
    }
}
