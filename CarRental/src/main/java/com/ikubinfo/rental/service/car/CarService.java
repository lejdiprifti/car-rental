package com.ikubinfo.rental.service.car;

import com.ikubinfo.rental.service.car.dto.CarFilter;
import com.ikubinfo.rental.service.car.dto.CarModel;
import com.ikubinfo.rental.service.car.dto.CarsPage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarService {

    CarsPage getAllCars(CarFilter carFilter);

    CarsPage getAllAvailableCars(CarFilter carFilter);

    CarModel getById(Long id);

    List<CarModel> getByCategory(Long categoryId);

    CarModel save(CarModel model, MultipartFile photo);

    void edit(CarModel model, MultipartFile photo, Long id);

    void delete(Long id);
}
