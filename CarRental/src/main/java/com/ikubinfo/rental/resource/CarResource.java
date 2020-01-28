package com.ikubinfo.rental.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.service.CarService;

@RestController
@RequestMapping(path="/cars", produces="application/json")
public class CarResource {
	
	@Autowired
	private CarService carService;
	
	public CarResource() {
		
	}
	
	@GetMapping
	public ResponseEntity<List<CarModel>> getAll(){
		return new ResponseEntity<List<CarModel>>(carService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CarModel> getById(Long id){
		return new ResponseEntity<CarModel>(carService.getById(id), HttpStatus.OK);
	}
	
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@RequestBody CarModel model) {
		carService.save(model);
	}
	
	@PutMapping(path="/{id}", consumes = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void edit(@RequestBody CarModel model, @PathVariable("id") Long id) {
		carService.edit(model, id);
	}
	
	@DeleteMapping(path="/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		carService.delete(id);
	}
}
