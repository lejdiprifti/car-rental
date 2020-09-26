package com.ikubinfo.rental.service.category.converter;

import com.ikubinfo.rental.service.category.dto.CategoryEntity;
import com.ikubinfo.rental.service.category.dto.CategoryModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryConverter {

    @Autowired
    private ModelMapper modelMapper;

    public CategoryModel toModel(CategoryEntity entity) {
        return modelMapper.map(entity, CategoryModel.class);
    }

    public CategoryEntity toEntity(CategoryModel model) {
        return modelMapper.map(model, CategoryEntity.class);
    }

    public List<CategoryModel> toModel(List<CategoryEntity> entityList) {
        List<CategoryModel> modelList = new ArrayList<>();
        for (CategoryEntity entity : entityList) {
            modelList.add(toModel(entity));
        }
        return modelList;
    }
}
