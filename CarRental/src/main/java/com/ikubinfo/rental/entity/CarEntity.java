package com.ikubinfo.rental.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ikubinfo.rental.model.enums.StatusEnum;
import lombok.Data;

@Entity
@Table(name = "car", schema = "rental")
@Data
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "car_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "plate")
    private String plate;

    @Column(name = "year")
    private int year;

    @Column(name = "type")
    private String type;

    @Column(name = "description", length = 10000)
    private String description;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "price")
    private double price;

    @Column(name = "diesel")
    private String diesel;

    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    private StatusEnum availability;
    private boolean active;

}
