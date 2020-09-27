package com.ikubinfo.rental.controller;

import com.ikubinfo.rental.service.reservation.ReservationService;
import com.ikubinfo.rental.service.reservation.dto.ReservationFilter;
import com.ikubinfo.rental.service.reservation.dto.ReservationModel;
import com.ikubinfo.rental.service.reservation.dto.ReservationPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ikubinfo.rental.controller.constants.ApiConstants.CLIENT_APP;
import static com.ikubinfo.rental.controller.constants.ApiConstants.RESERVATION_PATH;

@RestController
@RequestMapping(path = RESERVATION_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationModel>> getAll() {
        return new ResponseEntity<>(reservationService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationModel> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(reservationService.getById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/user")
    public ResponseEntity<ReservationPage> getByUsername(@RequestBody ReservationFilter reservationFilter) {
        return new ResponseEntity<>(
                reservationService.getByUsername(reservationFilter), HttpStatus.OK);
    }

    @PostMapping(path = "/add", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody ReservationModel model) {
        reservationService.save(model);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@RequestBody ReservationModel model, @PathVariable("id") Long id) {
        reservationService.edit(model, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        reservationService.cancel(id);
    }

}
