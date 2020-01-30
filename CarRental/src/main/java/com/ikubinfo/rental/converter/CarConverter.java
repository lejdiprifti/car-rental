package com.ikubinfo.rental.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.model.CarModel;

@Component
public class CarConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CarConverter() {
		
	}
	
	public CarModel toModel(CarEntity entity) {
		return modelMapper.map(entity, CarModel.class);
	}
	
	public CarEntity toEntity(CarModel model) {
		return modelMapper.map(model, CarEntity.class);
	}
	
	public List<CarModel> toModel(List<CarEntity> entityList){
		List<CarModel> modelList = new ArrayList<CarModel>();
		for (CarEntity entity: entityList) {
			modelList.add(toModel(entity));
		}
		return modelList;
	}
}
