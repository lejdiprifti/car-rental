package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.model.CategoryPage;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(path = "/category", produces = "application/json")
@CrossOrigin("http://localhost:4200")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CarService carService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryModel>> getAll() {
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CategoryPage> getAll(@RequestParam(name = "startIndex") int startIndex,
                                               @RequestParam(name = "pageSize") int pageSize) {
        return new ResponseEntity<>(categoryService.getAll(startIndex, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(categoryService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/cars")
    public ResponseEntity<List<CarModel>> getCarsByCategory(@PathVariable("id") Long categoryId) {
        return new ResponseEntity<>(carService.getByCategory(categoryId), HttpStatus.OK);
    }

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestPart("properties") CategoryModel model, @RequestPart("file") MultipartFile file) {
        categoryService.save(model, file);
    }

    @PutMapping(path = "/{id}", consumes = {"application/json", "multipart/form-data"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@PathVariable("id") Long id, @RequestPart("properties") CategoryModel model,
                     @PathParam("file") MultipartFile file) {
        categoryService.edit(model, file, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }
}
