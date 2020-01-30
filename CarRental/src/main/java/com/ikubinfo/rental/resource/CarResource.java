package com.ikubinfo.rental.resource;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.service.CarService;

@RestController
@RequestMapping(path="/cars", produces="application/json")
@CrossOrigin("http://localhost:4200")
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
	public ResponseEntity<CarModel> getById(@PathVariable("id") Long id){
		return new ResponseEntity<CarModel>(carService.getById(id), HttpStatus.OK);
	}
	
	@PostMapping(consumes= {"multipart/form-data", "application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@RequestPart("properties") CarModel model, @RequestPart("file") MultipartFile file) {
		carService.save(model, file);
	}
	
	@PutMapping(path="/{id}", consumes = {"multipart/form-data", "application/json"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void edit(@RequestPart CarModel model, @PathVariable("id") Long id, @PathParam("file") MultipartFile file) {
		carService.edit(model,file, id);
	}
	
	@DeleteMapping(path="/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		carService.delete(id);
	}
}
