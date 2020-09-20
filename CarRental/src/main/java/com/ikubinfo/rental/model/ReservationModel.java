package com.ikubinfo.rental.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReservationModel {

    private Long id;
    private UserModel user;
    private CarModel car;
    private Long userId;
    private Long carId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Date created_at;
    private double fee;
    private boolean active;
}
