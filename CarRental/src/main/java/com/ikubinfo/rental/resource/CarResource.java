package com.ikubinfo.rental.resource;

import java.time.LocalDateTime;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CarsPage;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.ReservationService;

@RestController
@RequestMapping(path="/cars", produces="application/json")
@CrossOrigin("http://localhost:4200")
public class CarResource {
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private ReservationService reservationService;
	
	public CarResource() {
		
	}
	
	@GetMapping
	public ResponseEntity<CarsPage> getAll(@RequestParam("startIndex") int startIndex, 
			@RequestParam("pageSize") int pageSize,
			@RequestParam(name="selectedCategories",required=false) List<Long> selectedCategoryIds,
			@RequestParam(name="startDate", required=false) String startDate,
			@RequestParam(name="endDate",required=false) String endDate
			){
		return new ResponseEntity<CarsPage>(carService.getAllCars(startIndex, pageSize,selectedCategoryIds, startDate,endDate), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CarModel> getById(@PathVariable("id") Long id){
		return new ResponseEntity<CarModel>(carService.getById(id), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/reservations")
	public ResponseEntity<List<ReservationModel>> getReservationsByCar(@PathVariable("id") Long carId){
		return new ResponseEntity<List<ReservationModel>>(reservationService.getByCar(carId), HttpStatus.OK);
	}
	
	@PostMapping(consumes= {"multipart/form-data", "application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@RequestPart("properties") CarModel model, @RequestPart("file") MultipartFile file) {
		carService.save(model, file);
	}
	
	@PutMapping(path="/{id}", consumes = {"multipart/form-data", "application/json"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void edit(@RequestPart("properties") CarModel model,@PathParam("file") MultipartFile file, @PathVariable("id") Long id) {
		carService.edit(model,file, id);
	}

	@PutMapping(path="/{id}/reservations")
	public ResponseEntity<Integer> cancelReservationsByCar(@RequestBody LocalDateTime date, @PathVariable("id") Long carId) {
		return new ResponseEntity<Integer>(reservationService.cancelByCarAndDate(date, carId), HttpStatus.OK);
	}
	
	@DeleteMapping(path="/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		carService.delete(id);
	}
}
