package com.ikubinfo.rental.model;

import lombok.Data;

import java.util.List;

@Data
public class ReservationPage {

    private Long totalRecords;
    private List<ReservationModel> reservationList;
}
