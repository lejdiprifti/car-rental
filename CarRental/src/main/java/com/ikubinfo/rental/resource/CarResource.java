package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CarsPage;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/cars", produces = "application/json")
@CrossOrigin("http://localhost:4200")
public class CarResource {

    @Autowired
    private CarService carService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<CarsPage> getAll(@RequestParam("startIndex") int startIndex,
                                           @RequestParam("pageSize") int pageSize,
                                           @RequestParam(name = "selectedCategories", required = false) List<Long> selectedCategoryIds,
                                           @RequestParam(name = "startDate", required = false) String startDate,
                                           @RequestParam(name = "endDate", required = false) String endDate,
                                           @RequestParam(name = "brand", required = false) String brand
    ) {
        return new ResponseEntity<>(carService.getAllCars(startIndex, pageSize, selectedCategoryIds, startDate, endDate, brand), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarModel> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(carService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationModel>> getReservationsByCar(@PathVariable("id") Long carId) {
        return new ResponseEntity<>(reservationService.getByCar(carId), HttpStatus.OK);
    }

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
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
        return new ResponseEntity<Integer>(reservationService.cancelByCarAndDate(date, carId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        carService.delete(id);
    }
}
