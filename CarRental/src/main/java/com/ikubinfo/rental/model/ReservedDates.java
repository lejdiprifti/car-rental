package com.ikubinfo.rental.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservedDates {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
