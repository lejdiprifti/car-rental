package com.ikubinfo.rental.service.role.converter;

import com.ikubinfo.rental.service.role.dto.RoleEntity;
import com.ikubinfo.rental.service.role.dto.RoleModel;
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
