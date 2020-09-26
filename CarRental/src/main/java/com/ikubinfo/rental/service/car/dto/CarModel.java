package com.ikubinfo.rental.service.car.dto;

import com.ikubinfo.rental.service.category.dto.CategoryModel;
import com.ikubinfo.rental.service.reservation.dto.ReservedDates;
import com.ikubinfo.rental.service.car.status.StatusEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
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
