package com.ikubinfo.rental.model;

import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

public class CategoryModel {
	
	private Long id;
	private MultipartFile file;
	private String name;
	private String description;
	private boolean active; 
	private byte[] photo;
	
	public CategoryModel() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "CategoryModel [id=" + id + ", file=" + file + ", name=" + name + ", description=" + description
				+ ", active=" + active + ", photo=" + Arrays.toString(photo) + "]";
	}

	 

}
