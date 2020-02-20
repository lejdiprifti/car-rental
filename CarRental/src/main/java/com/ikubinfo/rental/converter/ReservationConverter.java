package com.ikubinfo.rental.converter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.UserModel;

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
	
	public ReservationModel toModel(Object[] object) {
		ReservationModel model = new ReservationModel();
		model.setId((Long) object[0]);
		model.setStartDate((LocalDateTime) object[1]);
		model.setEndDate((LocalDateTime) object[2]);
		model.setCar(new CarModel());
		model.getCar().setId((Long) object[3]);
		model.getCar().setName((String) object[4]);
		model.getCar().setType((String) object[5]);
		model.getCar().setPrice((double) object[6]);
		model.setUser(new UserModel());
		model.getUser().setId((Long) object[7]);
		model.getUser().setFirstName((String) object[8]);
		model.getUser().setLastName((String) object[9]);
		model.getCar().setPlate((String) object[10]);
		if (object.length > 11) {
		model.getCar().setPhoto((byte[]) object[11]);
		}
		return model;
	}
	
	
	public List<ReservationModel> toModelObject(List<Object[]> objectList){
		List<ReservationModel> modelList = new ArrayList<ReservationModel>();
		for (Object[] entity: objectList) {
			modelList.add(toModel(entity));
		}
		return modelList;
	}
	
}
