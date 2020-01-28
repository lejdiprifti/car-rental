package com.ikubinfo.rental.model;



import org.springframework.web.multipart.MultipartFile;

public class CarModel {
	
	private Long id;
	private String name;
	private String type;
	private MultipartFile photo;
	private String plate;
	private double price;
	private String diesel;
	private String description;
	private CategoryModel category;
	private Long categoryId;
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


	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "CarModel [id=" + id + ", name=" + name + ", type=" + type + ", photo=" + photo + ", plate=" + plate
				+ ", price=" + price + ", diesel=" + diesel + ", category=" + category + ", availability="
				+ availability + ", active=" + active + "]";
	}

}
