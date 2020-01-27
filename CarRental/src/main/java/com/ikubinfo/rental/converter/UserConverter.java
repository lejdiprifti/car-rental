package com.ikubinfo.rental.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ikubinfo.rental.entity.UserEntity;
import com.ikubinfo.rental.model.UserModel;

@Component
public class UserConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public UserConverter() {
		
	}
	
	public UserModel toModel(UserEntity entity) {
		return modelMapper.map(entity, UserModel.class);
	}
	
	public UserEntity toEntity(UserModel model) {
		return modelMapper.map(model, UserEntity.class);
	}
	
	public List<UserModel> toModel(List<UserEntity> entityList){
		List<UserModel> modelList = new ArrayList<UserModel>();
		for (UserEntity entity: entityList) {
			modelList.add(toModel(entity));
		}
		return modelList;
	}
}
