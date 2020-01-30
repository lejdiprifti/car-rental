package com.ikubinfo.rental.resource;

import java.io.IOException;
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
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.CategoryService;

@RestController
@RequestMapping(path = "/category", produces = "application/json")
@CrossOrigin("http://localhost:4200")
public class CategoryResource {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CarService carService;

	public CategoryResource() {

	}

	@GetMapping
	public ResponseEntity<List<CategoryModel>> getAll() {
		return new ResponseEntity<List<CategoryModel>>(categoryService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryModel> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<CategoryModel>(categoryService.getById(id), HttpStatus.OK);
	}

	@GetMapping("/{id}/cars")
	public ResponseEntity<List<CarModel>> getCarsByCategory(@PathVariable("id") Long categoryId) {
		return new ResponseEntity<List<CarModel>>(carService.getByCategory(categoryId), HttpStatus.OK);
	}

	@PostMapping(consumes = {"multipart/form-data", "application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@RequestPart("properties") CategoryModel model, @RequestPart("file") MultipartFile file) throws IOException {
		categoryService.save(model, file);
	}

	@PutMapping(path = "/{id}", consumes = { "application/json", "multipart/form-data" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void edit(@PathVariable("id") Long id, @RequestPart("properties") CategoryModel model, @PathParam("file") MultipartFile file) throws IOException {
		categoryService.edit(model,file, id);
	}

	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		categoryService.delete(id);
	}
}
