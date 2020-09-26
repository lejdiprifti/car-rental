package com.ikubinfo.rental.service;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
import com.ikubinfo.rental.exceptions.messages.NotFound;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.ReservedDates;
import com.ikubinfo.rental.model.enums.StatusEnum;
import com.ikubinfo.rental.model.page.CarsPage;
import com.ikubinfo.rental.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.ikubinfo.rental.controller.filter.FilterUtils.getFilterData;
import static com.ikubinfo.rental.converter.CarConverter.*;

@Service
public class CarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReservationService reservationService;

    public CarsPage getAllCars(int startIndex, int pageSize, List<Long> selectedCategoryIds, String startDate,
                               String endDate, String brand) {
        LocalDateTime startDate2 = getFilterData(startDate, endDate).get("startDate");
        LocalDateTime endDate2 = getFilterData(startDate, endDate).get("endDate");
        CarsPage carPage = new CarsPage();
        authorizationService.isUserAuthorized();
        carPage.setCarsList(getAll(startIndex, pageSize, selectedCategoryIds, startDate2, endDate2, brand));
        carPage.setTotalRecords(carRepository.countAllCars(selectedCategoryIds, startDate2, endDate2, brand));
        return carPage;
    }

    public CarsPage getAllAvailableCars(int startIndex, int pageSize, List<Long> selectedCategoryIds, String startDate,
                                        String endDate, String brand) {
        LOGGER.info("Getting all available cars with startIndex {}, pageSize {}, startDate {}, endDate {}, brand {}", startIndex, pageSize, startDate, endDate, brand);
        LocalDateTime startDate2 = getFilterData(startDate, endDate).get("startDate");
        LocalDateTime endDate2 = getFilterData(startDate, endDate).get("endDate");
        CarsPage carPage = new CarsPage();
        carPage.setCarsList(
                getAllAvailable(startIndex, pageSize, selectedCategoryIds, startDate2, endDate2, brand));
        carPage.setTotalRecords(carRepository.countAvailableCars(selectedCategoryIds, startDate2, endDate2, brand));
        return carPage;
    }

    private List<CarModel> getAll(int startIndex, int pageSize, List<Long> selectedCategoryIds, LocalDateTime startDate,
                                  LocalDateTime endDate, String brand) {
        List<CarModel> modelList = toModelObject(
                carRepository.getAll(startIndex, pageSize, selectedCategoryIds, startDate, endDate, brand));
        for (CarModel car : modelList) {
            setReservedDatesToCar(car);
        }
        return modelList;
    }

    private List<CarModel> getAllAvailable(int startIndex, int pageSize, List<Long> selectedCategoryIds,
                                           LocalDateTime startDate, LocalDateTime endDate, String brand) {
        List<CarModel> modelList = toModelObject(
                carRepository.getAllAvailable(startIndex, pageSize, selectedCategoryIds, startDate, endDate, brand));
        for (CarModel car : modelList) {
            setReservedDatesToCar(car);
        }
        return modelList;
    }

    private void setReservedDatesToCar(CarModel car) {
        List<ReservedDates> carReservedDatesList = reservationService.getReservedDatesByCar(car.getId());
        car.setReservedDates(carReservedDatesList);
    }


    public CarModel getById(Long id) {
        try {
            CarModel car = toModel(carRepository.getById(id));
            setReservedDatesToCar(car);
            return car;
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.CAR_NOT_FOUND.getErrorMessage());
        }
    }

    public List<CarModel> getByCategory(Long categoryId) {
        return toModel(carRepository.getByCategory(categoryId));
    }

    public CarModel save(CarModel model, MultipartFile photo) {
        authorizationService.isUserAuthorized();
        validateCarData(model, photo);
        checkIfSaveIsAvailable(model.getPlate());
        return executeSaveCar(model, photo);
    }

    private CarModel executeSaveCar(CarModel model, MultipartFile photo) {
        try {
            CarEntity entity = toEntity(model);
            entity.setPhoto(photo.getBytes());
            entity.setActive(true);
            CarEntity carEntity = carRepository.save(entity);
            return toModel(carEntity);
        } catch (IOException e) {
            throw new CarRentalBadRequestException("Something bad happened saving car. Please, try again later.");
        }
    }

    public void edit(CarModel model, MultipartFile photo, Long id) {
        authorizationService.isUserAuthorized();
        validateCarData(model, photo);
        checkIfCarExists(id);
        checkIfUpdateIsAvailable(model.getPlate(), id);
        executeUpdateCar(model, photo, id);
    }

    private void executeUpdateCar(CarModel carModel, MultipartFile photo, Long carId) {
        try {
            CarEntity entity = toEntity(carModel);
            if (photo != null) {
                entity.setPhoto(photo.getBytes());
            } else {
                entity.setPhoto(getById(carId).getPhoto());
            }
            entity.setActive(true);
            carRepository.edit(entity);
        } catch (IOException e) {
            throw new CarRentalBadRequestException("Something bad happened. Please, try again later.");
        }
    }

    public void delete(Long id) {
        authorizationService.isUserAuthorized();
        hasActiveReservations(id);
        executeDeleteCar(id);
    }

    private void executeDeleteCar(Long carId) {
        CarEntity entity = toEntity(getById(carId));
        entity.setActive(false);
        carRepository.edit(entity);
    }

    private void checkIfSaveIsAvailable(String plate) {
        try {
            carRepository.getByPlate(plate);
            LOGGER.info("Another car exists with the same plate.");
            throw new CarRentalBadRequestException(BadRequest.CAR_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.info("No car exists with the plate {}", plate);
        }
    }

    private void checkIfUpdateIsAvailable(String plate, Long id) {
        try {
            carRepository.checkIfExistsAnother(plate, id);
            LOGGER.info("Another car exists with the same plate.");
            throw new CarRentalBadRequestException(BadRequest.CAR_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.info("No car exists with the plate {} except of car {} ", plate, id);
        }
    }

    private void checkIfCarExists(Long carId) {
        try {
            carRepository.getById(carId);
            LOGGER.info("Car with id {} exists", carId);
        } catch (NoResultException exception) {
            LOGGER.info("Car with id {} does not exists", carId);
            throw new CarRentalNotFoundException(NotFound.CAR_NOT_FOUND.getErrorMessage());
        }
    }

    private void hasActiveReservations(Long carId) {
        if (carRepository.countActiveReservationsByCar(carId) > 0) {
            throw new CarRentalBadRequestException(BadRequest.CAR_HAS_ACTIVE_RESERVATIONS.getErrorMessage());
        }
    }

    private void validateCarData(CarModel model, MultipartFile file) {
        try {
            categoryService.checkIfCategoryExists(model.getCategoryId());
            if (model.getName().trim().equals("")) {
                throw new CarRentalBadRequestException(BadRequest.NAME_REQUIRED.getErrorMessage());
            }
            if (model.getDescription().trim().equals("") || model.getDescription().length() > 10000) {
                throw new CarRentalBadRequestException(BadRequest.DESCRIPTION_REQUIRED.getErrorMessage());
            }
            if (model.getAvailability() == null) {
                throw new CarRentalBadRequestException(BadRequest.AVAILABILITY_REQUIRED.getErrorMessage());
            }
            if (model.getDiesel().trim().equals("")) {
                throw new CarRentalBadRequestException(BadRequest.DIESEL_REQUIRED.getErrorMessage());
            }
            if (model.getPlate().trim().equals("")) {
                throw new CarRentalBadRequestException(BadRequest.PLATE_REQUIRED.getErrorMessage());
            }
            if (model.getPrice() < 0) {
                throw new CarRentalBadRequestException(BadRequest.PRICE_POSITIVE.getErrorMessage());
            }
            if (model.getYear() < 1769) {
                throw new CarRentalBadRequestException(BadRequest.YEAR_MUST_BE_VALID.getErrorMessage());
            }
            if (file == null) {
                if (model.getId() == null) {
                    throw new CarRentalBadRequestException(BadRequest.PHOTO_REQUIRED.getErrorMessage());
                }
            } else if (file.getSize() > 100000) {
                throw new CarRentalBadRequestException(BadRequest.PHOTO_TO_BE_LESS_KB.getErrorMessage());
            }
            if (model.getType().equals("")) {
                throw new CarRentalBadRequestException(BadRequest.BRAND_REQUIRED.getErrorMessage());
            }
            if (!model.getAvailability().equals(StatusEnum.AVAILABLE)
                    && !model.getAvailability().equals(StatusEnum.SERVIS)) {
                throw new CarRentalBadRequestException(BadRequest.AVAILABILITY_REQUIRED.getErrorMessage());
            }
        } catch (NullPointerException e) {
            throw new CarRentalBadRequestException(BadRequest.USER_DATA_MISSING.getErrorMessage());
        }
    }
}
