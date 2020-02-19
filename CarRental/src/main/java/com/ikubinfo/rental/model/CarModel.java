package com.ikubinfo.rental.model;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ikubinfo.rental.entity.StatusEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarModel {
	
	private Long id;
	private String name;
	private String type;
	private MultipartFile file;
	private String plate;
	private double price;
	private String diesel;
	private String description;
	private int year;
	private CategoryModel category;
	private Long categoryId;
	private byte[] photo;
	private List<ReservedDates> reservedDates;
	private StatusEnum availability;
	private boolean active;	
}
