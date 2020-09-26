package com.ikubinfo.rental.model.page;

import com.ikubinfo.rental.model.ReservationModel;
import lombok.Data;

import java.util.List;

@Data
public class ReservationPage {

    private Long totalRecords;
    private List<ReservationModel> reservationList;
}
