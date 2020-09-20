package com.ikubinfo.rental.model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class CarsPage {
	private Long totalRecords;
	private List<CarModel> carsList;
}
