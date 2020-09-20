package com.ikubinfo.rental.entity;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "category", schema = "rental")
@Data
public class CategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "category_id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Lob
	@Column(name = "photo")
	private byte[] photo;

	@Column(name = "active")
	private boolean active;

}