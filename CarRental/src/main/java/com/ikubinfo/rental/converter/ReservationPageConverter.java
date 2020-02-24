package com.ikubinfo.rental.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.ReservationPage;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ReservationPageConverter {
		
	public ReservationPage toModel(List<ReservationModel> reservationList, Long totalRecords) {
		ReservationPage reservationPage = new ReservationPage();
		reservationPage.setReservationList(reservationList);
		reservationPage.setTotalRecords(totalRecords);
		return reservationPage;
	}
}
