package com.ikubinfo.rental.service.car.dto;

import java.util.List;

import com.ikubinfo.rental.service.car.dto.CarModel;
import lombok.Data;

@Data
public class CarsPage {
	private Long totalRecords;
	private List<CarModel> carsList;
}
