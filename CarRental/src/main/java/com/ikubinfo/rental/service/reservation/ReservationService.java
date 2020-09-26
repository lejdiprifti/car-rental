package com.ikubinfo.rental.service.reservation;

import com.ikubinfo.rental.service.reservation.dto.ReservationModel;
import com.ikubinfo.rental.service.reservation.dto.ReservationPage;
import com.ikubinfo.rental.service.reservation.dto.ReservedDates;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    List<ReservationModel> getAll();

    ReservationPage getByUsername(int startIndex, int pageSize, String carName, String startDate,
                                  String endDate);

    List<ReservationModel> getByUsername();

    ReservationModel getById(Long id);

    List<ReservationModel> getByCar(Long carId);

    ReservationModel save(ReservationModel model);

    void edit(ReservationModel toUpdateModel, Long id);

    void cancel(Long id);

    int cancelByCarAndDate(LocalDateTime date, Long carId);

    List<ReservedDates> getReservedDatesByCar(Long carId);
}
