package com.ikubinfo.rental.service.car;

import com.ikubinfo.rental.service.car.dto.CarModel;
import com.ikubinfo.rental.service.car.dto.CarsPage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarService {

    CarsPage getAllCars(int startIndex, int pageSize, List<Long> selectedCategoryIds, String startDate,
                        String endDate, String brand);

    CarsPage getAllAvailableCars(int startIndex, int pageSize, List<Long> selectedCategoryIds, String startDate,
                                 String endDate, String brand);

    CarModel getById(Long id);

    List<CarModel> getByCategory(Long categoryId);

    CarModel save(CarModel model, MultipartFile photo);

    void edit(CarModel model, MultipartFile photo, Long id);

    void delete(Long id);
}
