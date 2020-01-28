package com.ikubinfo.rental.resource;

import java.io.IOException;
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

import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.service.CategoryService;

@RestController
@RequestMapping(path="/category", produces="application/json")
public class CategoryResource {
	
	@Autowired
	private CategoryService categoryService;
	
	public CategoryResource() {
		
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryModel>> getAll(){
		return new ResponseEntity<List<CategoryModel>>(categoryService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryModel> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<CategoryModel>(categoryService.getById(id), HttpStatus.OK);
	}
	
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@RequestBody CategoryModel model) throws IOException {
		categoryService.save(model);
	}
	
	@PutMapping(path="/{id}",consumes= {"application/json","multipart/form-data"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void edit(@RequestBody CategoryModel model, @PathVariable("id") Long id) throws IOException {
		categoryService.edit(model, id);
	}
	
	@DeleteMapping(path="/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		categoryService.delete(id);
	}
}
