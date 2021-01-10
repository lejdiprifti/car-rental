package com.ikubinfo.rental.service.car.repository;

import com.ikubinfo.rental.service.car.dto.CarEntity;
import com.ikubinfo.rental.service.car.dto.CarFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface CarRepository {

    CarEntity getById(Long carId);

    List<Object[]> getAll(CarFilter carFilter);

    Long countAvailableCars(CarFilter carFilter);

    Long countAllCars(CarFilter carFilter);

    List<Object[]> getAllAvailable(CarFilter carFilter);

    CarEntity getByPlate(String plate);

    List<CarEntity> getByCategory(Long categoryId);

    Long countFreeCars();

    CarEntity save(CarEntity entity);

    void edit(CarEntity entity);

    Long countRentedCars();

    void checkIfExistsAnother(String plate, Long id);

    Long countActiveReservationsByCar(Long carId);
}
