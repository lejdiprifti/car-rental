package com.ikubinfo.rental.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.model.ReservationModel;

@Component
public class ReservationConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ReservationConverter() {
		
	}
	
	public ReservationModel toModel(ReservationEntity entity) {
		return modelMapper.map(entity, ReservationModel.class);
	}
	
	public ReservationEntity toEntity(ReservationModel model) {
		return modelMapper.map(model, ReservationEntity.class);
	}
	
	public List<ReservationModel> toModel(List<ReservationEntity> entityList){
		List<ReservationModel> modelList = new ArrayList<ReservationModel>();
		for (ReservationEntity entity: entityList) {
			modelList.add(toModel(entity));
		}
		return modelList;
	}
	
	
}
