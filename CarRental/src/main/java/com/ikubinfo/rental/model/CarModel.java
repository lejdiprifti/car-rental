package com.ikubinfo.rental.model;

import java.util.Arrays;

public class CarModel {
	
	private Long id;
	private String name;
	private String type;
	private byte[] photo;
	private double price;
	private String diesel;
	private CategoryModel category;
	private boolean availability; 
	private boolean active;
	
	public CarModel() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDiesel() {
		return diesel;
	}

	public void setDiesel(String diesel) {
		this.diesel = diesel;
	}

	public CategoryModel getCategory() {
		return category;
	}

	public void setCategory(CategoryModel category) {
		this.category = category;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "CarModel [id=" + id + ", name=" + name + ", type=" + type + ", photo=" + Arrays.toString(photo)
				+ ", price=" + price + ", diesel=" + diesel + ", availability=" + availability + ", active=" + active
				+ "]";
	}
	
	
}
