package com.ikubinfo.rental.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class CategoryModel {
	
	private Long id;
	private MultipartFile file;
	private String name;
	private String description;
	private boolean active; 
	private byte[] photo;
}
