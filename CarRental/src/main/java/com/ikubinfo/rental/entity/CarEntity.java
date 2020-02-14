package com.ikubinfo.rental.entity;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="car", schema="rental")
public class CarEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="car_id")
	private Long id;
	
	@NotNull(message="Name is mandatory.")
	@Column(name="name")
	private String name;
	
	@NotNull(message="Plate is mandatory.")
	@Column(name="plate")
	private String plate;
	
	@NotNull(message="Year is mandatory.")
	@Column(name="year")
	private int year;
	
	@NotNull(message="Brand is mandatory.")
	@Column(name="type")
	private String type;
	
	@NotNull(message="Description is mandatory.")
	@Column(name="description")
	@Size(max=10000)
	private String description;
	
	@Lob
	@Column(name="photo", length = 100000)
	private byte[] photo;
	
	@NotNull(message="Price is mandatory.")
	@Column(name="price")
	private double price;
	
	@NotNull(message="Diesel is mandatory.")
	@Column(name="diesel")
	private String diesel;
	
	@ManyToOne
	@JoinColumn(name="category")
	private CategoryEntity category;
	
	@NotNull(message="Availability is mandatory.")
	private StatusEnum availability;
	
	@NotNull(message="Actice is mandatory.")
	private boolean active;
	
	public CarEntity() {
		
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}



	public StatusEnum getAvailability() {
		return availability;
	}

	public void setAvailability(StatusEnum availability) {
		this.availability = availability;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "CarEntity [id=" + id + ", name=" + name + ", plate=" + plate + ", year=" + year + ", type=" + type
				+ ", description=" + description + ", photo=" + Arrays.toString(photo) + ", price=" + price
				+ ", diesel=" + diesel + ", category=" + category + ", availability=" + availability + ", active="
				+ active + "]";
	}

	
	
}
