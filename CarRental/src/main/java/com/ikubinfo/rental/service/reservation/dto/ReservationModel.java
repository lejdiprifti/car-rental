package com.ikubinfo.rental.service.reservation.dto;

import com.ikubinfo.rental.service.user.dto.UserModel;
import com.ikubinfo.rental.service.car.dto.CarModel;
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
