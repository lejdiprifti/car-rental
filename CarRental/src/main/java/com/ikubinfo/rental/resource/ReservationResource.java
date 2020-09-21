package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.ReservationPage;
import com.ikubinfo.rental.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ikubinfo.rental.resource.constants.ApiConstants.CLIENT_APP;
import static com.ikubinfo.rental.resource.constants.ApiConstants.RESERVATION_PATH;

@RestController
@RequestMapping(path = RESERVATION_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class ReservationResource {

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

    @GetMapping(path = "/user", params = {"startIndex", "pageSize"})
    public ResponseEntity<ReservationPage> getByUsername(@RequestParam("startIndex") int startIndex,
                                                         @RequestParam("pageSize") int pageSize, @RequestParam(name = "carName", required = false, defaultValue = "") String carName,
                                                         @RequestParam(name = "startDate", defaultValue = "1900-01-01 00:00:00") String startDate,
                                                         @RequestParam(name = "endDate", defaultValue = "2900-01-01 00:00:00") String endDate) {
        return new ResponseEntity<>(
                reservationService.getByUsername(startIndex, pageSize, carName, startDate, endDate), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
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
