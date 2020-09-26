package com.ikubinfo.rental.model.page;

import java.util.List;

import com.ikubinfo.rental.model.CarModel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class CarsPage {
	private Long totalRecords;
	private List<CarModel> carsList;
}
