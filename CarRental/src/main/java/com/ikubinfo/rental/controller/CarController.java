package com.ikubinfo.rental.controller;

import com.ikubinfo.rental.service.authorization.AuthorizationService;
import com.ikubinfo.rental.service.car.CarService;
import com.ikubinfo.rental.service.car.dto.CarFilter;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.car.dto.CarModel;
import com.ikubinfo.rental.service.car.dto.CarsPage;
import com.ikubinfo.rental.service.reservation.ReservationService;
import com.ikubinfo.rental.service.reservation.dto.ReservationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.List;

import static com.ikubinfo.rental.controller.constants.ApiConstants.CARS_PATH;
import static com.ikubinfo.rental.controller.constants.ApiConstants.CLIENT_APP;

@RestController
@RequestMapping(path = CARS_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<CarsPage> getAll(@RequestBody CarFilter carFilter) {
        try {
            authorizationService.isUserAuthorized();
            return new ResponseEntity<>(carService.getAllCars(carFilter), HttpStatus.OK);
        } catch (CarRentalBadRequestException exception) {
            return new ResponseEntity<>(carService.getAllAvailableCars(carFilter), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarModel> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(carService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationModel>> getReservationsByCar(@PathVariable("id") Long carId) {
        return new ResponseEntity<>(reservationService.getByCar(carId), HttpStatus.OK);
    }

    @PostMapping(path = "/add", consumes = {"multipart/form-data", "application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestPart("properties") CarModel model, @RequestPart("file") MultipartFile file) {
        carService.save(model, file);
    }

    @PutMapping(path = "/{id}", consumes = {"multipart/form-data", "application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@RequestPart("properties") CarModel model, @PathParam("file") MultipartFile file, @PathVariable("id") Long id) {
        carService.edit(model, file, id);
    }

    @PutMapping(path = "/{id}/reservations")
    public ResponseEntity<Integer> cancelReservationsByCar(@RequestBody LocalDateTime date, @PathVariable("id") Long carId) {
        return new ResponseEntity<>(reservationService.cancelByCarAndDate(date, carId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        carService.delete(id);
    }
}
