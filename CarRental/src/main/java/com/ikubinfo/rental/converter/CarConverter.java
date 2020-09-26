package com.ikubinfo.rental.converter;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.model.enums.StatusEnum;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CategoryModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class CarConverter {

    private static ModelMapper modelMapper = new ModelMapper();

    public static CarModel toModel(CarEntity entity) {
        return modelMapper.map(entity, CarModel.class);
    }

    public static CarEntity toEntity(CarModel model) {
        return modelMapper.map(model, CarEntity.class);
    }

    public static List<CarModel> toModel(List<CarEntity> entityList) {
        List<CarModel> modelList = new ArrayList<>();
        for (CarEntity entity : entityList) {
            modelList.add(toModel(entity));
        }
        return modelList;
    }

    public static CarModel toModelObject(Object[] object) {
        CarModel model = new CarModel();
        model.setId((Long) object[0]);
        model.setPhoto((byte[]) object[1]);
        model.setName((String) object[2]);
        model.setDescription((String) object[3]);
        model.setPlate((String) object[4]);
        model.setDiesel((String) object[5]);
        model.setType((String) object[6]);
        model.setYear((int) object[7]);
        model.setPrice((double) object[8]);
        model.setAvailability((StatusEnum) object[9]);
        CategoryModel category = new CategoryModel();
        category.setId((Long) object[10]);
        category.setName((String) object[11]);
        model.setCategory(category);
        return model;
    }

    public static List<CarModel> toModelObject(List<Object[]> objectList) {
        List<CarModel> modelList = new ArrayList<>();
        for (Object[] obj : objectList) {
            modelList.add(toModelObject(obj));
        }
        return modelList;
    }

    private CarConverter() {}
}
