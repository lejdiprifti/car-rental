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

@Entity
@Table(name="car", schema="rental")
public class CarEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="car_id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="plate")
	private String plate;
	
	@Column(name="year")
	private int year;
	
	@Column(name="type")
	private String type;
	
	@Column(name="description", length=10000)
	private String description;
	
	@Lob
	@Column(name="photo")
	private byte[] photo;
	
	@Column(name="price")
	private double price;
	
	@Column(name="diesel")
	private String diesel;
	
	@ManyToOne
	@JoinColumn(name="category")
	private CategoryEntity category;
	private boolean availability;
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

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
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
