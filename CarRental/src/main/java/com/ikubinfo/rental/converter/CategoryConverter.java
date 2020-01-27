package com.ikubinfo.rental.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ikubinfo.rental.entity.CategoryEntity;
import com.ikubinfo.rental.model.CategoryModel;

@Component
public class CategoryConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CategoryConverter() {
		
	}
	
	public CategoryModel toModel(CategoryEntity entity) {
		return modelMapper.map(entity, CategoryModel.class);
	}
	
	public CategoryEntity toEntity(CategoryModel model) {
		return modelMapper.map(model, CategoryEntity.class);
	}
	
	public List<CategoryModel> toModel(List<CategoryEntity> entityList){
		List<CategoryModel> modelList = new ArrayList<CategoryModel>();
		for (CategoryEntity entity: entityList) {
			modelList.add(toModel(entity));
		}
		return modelList;
	}
}
