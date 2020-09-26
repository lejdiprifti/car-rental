package com.ikubinfo.rental.service.reservation.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservedDates {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
