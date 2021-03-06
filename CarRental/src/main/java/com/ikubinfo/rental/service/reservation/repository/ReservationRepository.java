package com.ikubinfo.rental.service.reservation.repository;

import com.ikubinfo.rental.service.reservation.dto.ReservationEntity;
import com.ikubinfo.rental.service.reservation.dto.ReservationFilter;
import com.ikubinfo.rental.service.reservation.dto.ReservationModel;
import com.ikubinfo.rental.service.reservation.dto.ReservedDates;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

public interface ReservationRepository {

    List<Object[]> getAll();

    ReservationEntity getById(Long id);

    List<Object[]> getByUser(String username, ReservationFilter reservationFilter);

    List<Object[]> getByUser(String username);

    List<ReservedDates> getReservedDatesByCar(Long carId);

    List<ReservationEntity> getByCar(Long carId);

    Long checkIfAvailable(ReservationModel reservationModel);

    Long updateIfAvailable(ReservationEntity reservationEntity, LocalDateTime startDate, LocalDateTime endDate);

    Long countNewBookings(Calendar date);

    List<ReservationEntity> getByCarAndDate(LocalDateTime date, Long carId);

    Long countNumberOfReservationsByUsername(String username, ReservationFilter reservationFilter);

    ReservationEntity save(ReservationEntity entity);

    void edit(ReservationEntity entity);
}
