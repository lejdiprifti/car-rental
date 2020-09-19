package com.ikubinfo.rental.service;

import com.ikubinfo.rental.converter.CarConverter;
import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.exceptions.ActiveReservationsException;
import com.ikubinfo.rental.exceptions.CarAlreadyExistsException;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
import com.ikubinfo.rental.exceptions.messages.NotFound;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CarsPage;
import com.ikubinfo.rental.model.ReservedDates;
import com.ikubinfo.rental.repository.CarRepository;
import com.ikubinfo.rental.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
public class CarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarConverter carConverter;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CategoryService categoryService;

    public CarsPage getAllCars(int startIndex, int pageSize, List<Long> selectedCategoryIds, String startDate,
                               String endDate, String brand) {
        LocalDateTime startDate2 = getFilterData(startDate, endDate).get("startDate");
        LocalDateTime endDate2 = getFilterData(startDate, endDate).get("endDate");
        if (brand == null) {
            brand = "";
        }
        CarsPage carPage = new CarsPage();
        try {
            authorizationService.isUserAuthorized();
            carPage.setCarsList(getAll(startIndex, pageSize, selectedCategoryIds, startDate2, endDate2, brand));
            carPage.setTotalRecords(carRepository.countAllCars(selectedCategoryIds, startDate2, endDate2, brand));

        } catch (ResponseStatusException e) {
            carPage.setCarsList(
                    getAllAvailable(startIndex, pageSize, selectedCategoryIds, startDate2, endDate2, brand));
            carPage.setTotalRecords(carRepository.countAvailableCars(selectedCategoryIds, startDate2, endDate2, brand));
        }
        return carPage;
    }

    public List<CarModel> getAll(int startIndex, int pageSize, List<Long> selectedCategoryIds, LocalDateTime startDate,
                                 LocalDateTime endDate, String brand) {
        List<CarModel> modelList = carConverter.toModelObject(
                carRepository.getAll(startIndex, pageSize, selectedCategoryIds, startDate, endDate, brand));
        for (CarModel car : modelList) {
            car.setReservedDates(getReservedDatesByCar(car.getId()));
        }
        return modelList;
    }

    public List<CarModel> getAllAvailable(int startIndex, int pageSize, List<Long> selectedCategoryIds,
                                          LocalDateTime startDate, LocalDateTime endDate, String brand) {
        List<CarModel> modelList = carConverter.toModelObject(
                carRepository.getAllAvailable(startIndex, pageSize, selectedCategoryIds, startDate, endDate, brand));
        for (CarModel car : modelList) {
            car.setReservedDates(getReservedDatesByCar(car.getId()));
        }
        return modelList;
    }

    public HashMap<String, LocalDateTime> getFilterData(String startDate, String endDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        HashMap<String, LocalDateTime> dataMap = new HashMap<String, LocalDateTime>();
        LocalDateTime startDate2;
        LocalDateTime endDate2;
        if (startDate != null) {
            startDate2 = LocalDateTime.parse(startDate, dateFormatter);
        } else {
            startDate2 = LocalDateTime.parse("1900-01-01 00:00:00", dateFormatter);
        }
        dataMap.put("startDate", startDate2);
        if (endDate != null) {
            endDate2 = LocalDateTime.parse(endDate, dateFormatter);
        } else {
            endDate2 = LocalDateTime.parse("1900-01-01 00:00:00", dateFormatter);
        }
        dataMap.put("endDate", endDate2);
        return dataMap;
    }

    public List<ReservedDates> getReservedDatesByCar(Long carId) {
        return reservationRepository.getReservedDatesByCar(carId);
    }

    public CarModel getById(Long id) {
        try {
            CarModel car = carConverter.toModel(carRepository.getById(id));
            car.setReservedDates(getReservedDatesByCar(id));
            return car;
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.CAR_NOT_FOUND.getErrorMessage());
        }
    }

    public List<CarModel> getByCategory(Long categoryId) {
        return carConverter.toModel(carRepository.getByCategory(categoryId));
    }

    public CarModel save(CarModel model, MultipartFile file) {
        authorizationService.isUserAuthorized();
        try {
            validateCarData(model, file);
            saveIfAvailable(model.getPlate());
            CarEntity entity = carConverter.toEntity(model);
            entity.setPhoto(file.getBytes());
            entity.setActive(true);
            CarEntity carEntity = carRepository.save(entity);
            return carConverter.toModel(carEntity);
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.CATEGORY_NOT_FOUND.getErrorMessage());
        } catch (IOException e) {
            throw new CarRentalBadRequestException("Something bad happened saving car. Please, try again later.");
        }
    }

    public void edit(CarModel model, MultipartFile file, Long id) {
        authorizationService.isUserAuthorized();
        try {
            validateCarData(model, file);
            checkIfCarExists(id);
            updateIfAvailable(model.getPlate(), id);
            CarEntity entity = carConverter.toEntity(model);
            if (file != null) {
                entity.setPhoto(file.getBytes());
            } else {
                entity.setPhoto(getById(id).getPhoto());
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
        CarEntity entity = carConverter.toEntity(getById(id));
        entity.setActive(false);
        carRepository.edit(entity);
    }

    private void saveIfAvailable(String plate) throws CarAlreadyExistsException {
        try {
            carRepository.getByPlate(plate);
            LOGGER.debug("Another car exists with the same plate.");
            throw new CarRentalBadRequestException(BadRequest.CAR_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.debug("No car exists with the plate {}", plate);
        }
    }

    private void updateIfAvailable(String plate, Long id) {
        try {
            carRepository.checkIfExistsAnother(plate, id);
            LOGGER.debug("Another car exists with the same plate.");
            throw new CarRentalBadRequestException(BadRequest.CAR_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.debug("No car exists with the plate {} except of car {} ", plate, id);
        }
    }

    private void checkIfCarExists(Long carId) {
        try {
            carRepository.getById(carId);
            LOGGER.debug("Car with id {} exists", carId);
        } catch (NoResultException exception) {
            LOGGER.debug("Car with id {} does not exists", carId);
            throw new CarRentalNotFoundException(NotFound.CAR_NOT_FOUND.getErrorMessage());
        }
    }

    private void hasActiveReservations(Long carId) throws ActiveReservationsException {
        if (reservationRepository.countActiveReservationsByCar(carId) > 0) {
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
