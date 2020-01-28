package com.ikubinfo.rental.model;

import org.springframework.web.multipart.MultipartFile;

public class CategoryModel {
	
	private Long id;
	private MultipartFile photo;
	private String name;
	private String description;
	private boolean active; 
	
	public CategoryModel() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
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
		return "CategoryModel [id=" + id + ", photo=" + photo + ", name=" + name + ", description=" + description
				+ ", active=" + active + "]";
	}

}
