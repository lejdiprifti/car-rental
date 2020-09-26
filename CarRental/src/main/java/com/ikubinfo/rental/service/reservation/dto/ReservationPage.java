package com.ikubinfo.rental.service.reservation.dto;

import com.ikubinfo.rental.service.reservation.dto.ReservationModel;
import lombok.Data;

import java.util.List;

@Data
public class ReservationPage {

    private Long totalRecords;
    private List<ReservationModel> reservationList;
}
