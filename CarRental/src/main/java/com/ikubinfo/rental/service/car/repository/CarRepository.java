package com.ikubinfo.rental.service.car.repository;

import com.ikubinfo.rental.service.car.dto.CarEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository {

    CarEntity getById(Long carId);

    List<Object[]> getAll(int startIndex, int pageSize, List<Long> selectedCategoryIds, LocalDateTime startDate2,
                          LocalDateTime endDate2, String brand);

    Long countAvailableCars(List<Long> selectedCategoryIds, LocalDateTime startDate2, LocalDateTime endDate2,
                            String brand);

    Long countAllCars(List<Long> selectedCategoryIds, LocalDateTime startDate2, LocalDateTime endDate2,
                      String brand);

    List<Object[]> getAllAvailable(int startIndex, int pageSize, List<Long> selectedCategoryIds,
                                   LocalDateTime startDate2, LocalDateTime endDate2, String brand);

    CarEntity getByPlate(String plate);

    List<CarEntity> getByCategory(Long categoryId);

    Long countFreeCars();

    CarEntity save(CarEntity entity);

    void edit(CarEntity entity);

    Long countRentedCars();

    void checkIfExistsAnother(String plate, Long id);

    Long countActiveReservationsByCar(Long carId);
}
