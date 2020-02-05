package com.ikubinfo.rental.resource;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.service.ReservationService;

import freemarker.template.TemplateException;

@RestController
@RequestMapping(path="/reservations", produces="application/json")
@CrossOrigin("http://localhost:4200")
public class ReservationResource {
	
	@Autowired
	private ReservationService reservationService;
	
	public ReservationResource() {
		
	}
	
	@GetMapping
	public ResponseEntity<List<ReservationModel>> getAll(){
		return new ResponseEntity<List<ReservationModel>>(reservationService.getAll(),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReservationModel> getById(@PathVariable("id") Long id){
		return new ResponseEntity<ReservationModel>(reservationService.getById(id), HttpStatus.OK);
	}
	
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void save(@RequestBody ReservationModel model) throws MessagingException, IOException, TemplateException {
		reservationService.save(model);
	}
	
	@PutMapping(path="/{id}", consumes="application/json")
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
