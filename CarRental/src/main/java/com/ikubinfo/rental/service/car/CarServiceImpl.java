package com.ikubinfo.rental.service.car;

import com.ikubinfo.rental.service.authorization.AuthorizationService;
import com.ikubinfo.rental.service.car.converter.CarConverter;
import com.ikubinfo.rental.service.car.dto.CarEntity;
import com.ikubinfo.rental.service.car.dto.CarFilter;
import com.ikubinfo.rental.service.car.dto.CarModel;
import com.ikubinfo.rental.service.car.dto.CarsPage;
import com.ikubinfo.rental.service.car.repository.CarRepository;
import com.ikubinfo.rental.service.car.status.StatusEnum;
import com.ikubinfo.rental.service.category.CategoryService;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.service.exceptions.messages.BadRequest;
import com.ikubinfo.rental.service.exceptions.messages.NotFound;
import com.ikubinfo.rental.service.reservation.ReservationService;
import com.ikubinfo.rental.service.reservation.dto.ReservedDates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.ikubinfo.rental.service.filter.FilterUtils.getFormattedLocalDateTimes;

@Service
public class CarServiceImpl implements CarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarServiceImpl.class);
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarConverter carConverter;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReservationService reservationService;

    @Override
    public CarsPage getAllCars(CarFilter carFilter) {
        authorizationService.isUserAuthorized();
        HashMap<String, LocalDateTime> dateTimeHashMap = getFormattedLocalDateTimes(carFilter.getStartDate(), carFilter.getEndDate());
        carFilter.setBothLocalDateTimes(dateTimeHashMap);
        CarsPage carPage = new CarsPage();
        carPage.setCarsList(getAll(carFilter));
        carPage.setTotalRecords(carRepository.countAllCars(carFilter));
        return carPage;
    }

    @Override
    public CarsPage getAllAvailableCars(CarFilter carFilter) {
        LOGGER.info("Getting all available cars with startIndex {}, pageSize {}", carFilter.getStartIndex(), carFilter.getPageSize());
        HashMap<String, LocalDateTime> dateTimeHashMap = getFormattedLocalDateTimes(carFilter.getStartDate(), carFilter.getEndDate());
        carFilter.setBothLocalDateTimes(dateTimeHashMap);
        CarsPage carPage = new CarsPage();
        carPage.setCarsList(
                getAllAvailable(carFilter));
        carPage.setTotalRecords(carRepository.countAvailableCars(carFilter));
        return carPage;
    }

    private List<CarModel> getAll(CarFilter carFilter) {
        List<CarModel> modelList = carConverter.toModelObject(
                carRepository.getAll(carFilter));
        for (CarModel car : modelList) {
            setReservedDatesToCar(car);
        }
        return modelList;
    }

    private List<CarModel> getAllAvailable(CarFilter carFilter) {
        List<CarModel> modelList = carConverter.toModelObject(
                carRepository.getAllAvailable(carFilter));
        for (CarModel car : modelList) {
            setReservedDatesToCar(car);
        }
        return modelList;
    }

    private void setReservedDatesToCar(CarModel car) {
        List<ReservedDates> carReservedDatesList = reservationService.getReservedDatesByCar(car.getId());
        car.setReservedDates(carReservedDatesList);
    }


    @Override
    public CarModel getById(Long id) {
        try {
            CarModel car = carConverter.toModel(carRepository.getById(id));
            setReservedDatesToCar(car);
            return car;
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.CAR_NOT_FOUND.getErrorMessage());
        }
    }

    @Override
    public List<CarModel> getByCategory(Long categoryId) {
        return carConverter.toModel(carRepository.getByCategory(categoryId));
    }

    @Override
    public CarModel save(CarModel model, MultipartFile photo) {
        authorizationService.isUserAuthorized();
        validateCarData(model, photo);
        checkIfSaveIsAvailable(model.getPlate());
        return executeSaveCar(model, photo);
    }

    private CarModel executeSaveCar(CarModel model, MultipartFile photo) {
        try {
            CarEntity entity = carConverter.toEntity(model);
            entity.setPhoto(photo.getBytes());
            entity.setActive(true);
            CarEntity carEntity = carRepository.save(entity);
            return carConverter.toModel(carEntity);
        } catch (IOException e) {
            throw new CarRentalBadRequestException("Something bad happened saving car. Please, try again later.");
        }
    }

    @Override
    public void edit(CarModel model, MultipartFile photo, Long id) {
        LOGGER.info("Updating car with id {}", id);
        authorizationService.isUserAuthorized();
        validateCarData(model, photo);
        checkIfCarExists(id);
        checkIfUpdateIsAvailable(model.getPlate(), id);
        executeUpdateCar(model, photo, id);
    }

    private void executeUpdateCar(CarModel carModel, MultipartFile photo, Long carId) {
        try {
            CarEntity entity = carConverter.toEntity(carModel);
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

    @Override
    public void delete(Long id) {
        LOGGER.info("Executing deletion of car with id {}", id);
        authorizationService.isUserAuthorized();
        hasActiveReservations(id);
        executeDeleteCar(id);
    }

    private void executeDeleteCar(Long carId) {
        CarEntity entity = carConverter.toEntity(getById(carId));
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
            LOGGER.info("Validating data of car with plate {}", model.getPlate());
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
