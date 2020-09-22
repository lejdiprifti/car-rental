package com.ikubinfo.rental.service;

import com.ikubinfo.rental.converter.ReservationConverter;
import com.ikubinfo.rental.converter.ReservationPageConverter;
import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
import com.ikubinfo.rental.exceptions.messages.NotFound;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.ReservationPage;
import com.ikubinfo.rental.repository.ReservationRepository;
import com.ikubinfo.rental.security.JwtTokenUtil;
import com.ikubinfo.rental.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import static com.ikubinfo.rental.controller.filter.FilterUtils.getFilterData;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationConverter reservationConverter;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CarService carService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReservationPageConverter reservationPageConverter;

    public List<ReservationModel> getAll() {
        authorizationService.isUserAuthorized();
        return reservationConverter.toModelObject(reservationRepository.getAll());
    }

    public ReservationPage getByUsername(int startIndex, int pageSize, String carName, String startDate,
                                         String endDate) {
        LocalDateTime startDate2 = getFilterData(startDate, endDate).get("startDate");
        LocalDateTime endDate2 = getFilterData(startDate, endDate).get("endDate");
        List<ReservationModel> reservationList = reservationConverter.toModelObject(reservationRepository
                .getByUser(jwtTokenUtil.getUsername(), startIndex, pageSize, carName, startDate2, endDate2));
        Long totalRecords = reservationRepository.countNumberOfReservationsByUsername(jwtTokenUtil.getUsername(),
                carName, startDate2, endDate2);
        return reservationPageConverter.toModel(reservationList, totalRecords);
    }

    public List<ReservationModel> getByUsername() {
        return reservationConverter.toModelObject(reservationRepository.getByUser(jwtTokenUtil.getUsername()));
    }

    public ReservationModel getById(Long id) {
        try {
            return reservationConverter.toModel(reservationRepository.getById(id));
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.RESERVATION_NOT_FOUND.getErrorMessage());
        }
    }

    public List<ReservationModel> getByCar(Long carId) {
        return reservationConverter.toModel(reservationRepository.getByCar(carId));
    }

    public ReservationModel save(ReservationModel model) {
        checkDates(model.getStartDate(), model.getEndDate());
        checkIfReservationIsAvailable(model.getCarId(), model.getStartDate(), model.getEndDate());
        ReservationEntity entity = reservationConverter.toEntity(model);
        entity.setCreated_at(Calendar.getInstance());
        entity.setActive(true);
        entity.setUserId(userService.getByUsername(jwtTokenUtil.getUsername()).getId());
        ReservationEntity reservationEntity = reservationRepository.save(entity);
        sendNotificationMail(reservationEntity, model.getFee());
        return reservationConverter.toModel(reservationEntity);
    }

    public void edit(ReservationModel model, Long id) {
        ReservationEntity entity = reservationConverter.toEntity(getById(id));
        checkIfAuthorized(entity.getUserId());
        checkDates(model.getStartDate(), model.getEndDate());
        checkIfUpdateIsAvailable(entity, model.getStartDate(), model.getEndDate());
        entity.setCreated_at(Calendar.getInstance());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        reservationRepository.edit(entity);
        sendNotificationMail(entity, model.getFee());
    }

    private void sendNotificationMail(ReservationEntity reservationEntity, double fee) {
        CarModel carModel = carService.getById(reservationEntity.getCarId());
        emailService.sendConfirmationMail(reservationEntity, fee, carModel);
    }

    public void cancel(Long id) {
        ReservationEntity entity = reservationConverter.toEntity(getById(id));
        checkIfAuthorized(entity.getUserId());
        entity.setActive(false);
        reservationRepository.edit(entity);
    }

    private void checkIfUpdateIsAvailable(ReservationEntity reservationEntity, LocalDateTime startDate, LocalDateTime endDate) {
        Long reservations = reservationRepository.updateIfAvailable(reservationEntity, startDate, endDate);
        if (reservations > 0) {
            throw new CarRentalBadRequestException(BadRequest.CAR_RESERVED.getErrorMessage());
        }
    }

    private void checkIfReservationIsAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
        Long reservations = reservationRepository.checkIfAvailable(carId, startDate, endDate);
        if (reservations > 0) {
            throw new CarRentalBadRequestException(BadRequest.CAR_RESERVED.getErrorMessage());
        }
    }

    private void checkDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isBefore(LocalDateTime.now()) || endDate.isBefore(startDate)) {
            throw new CarRentalBadRequestException(BadRequest.INVALID_DATES.getErrorMessage());
        }
    }

    private void checkIfAuthorized(Long userId) {
        String reservationOwnerUsername = userService.getById(userId).getUsername();
        if (!reservationOwnerUsername.equals(jwtTokenUtil.getUsername())) {
            throw new CarRentalBadRequestException(BadRequest.UNAUTHORIZED.getErrorMessage());
        }
    }

    public int cancelByCarAndDate(LocalDateTime date, Long carId) {
        authorizationService.isUserAuthorized();
        List<ReservationEntity> reservationList = decideToCancelAllOrUntilDate(date, carId);
        cancelReservationsAndSendCancelMail(reservationList);
        return reservationList.size();
    }

    private void cancelReservationsAndSendCancelMail(List<ReservationEntity> reservationEntityList) {
        for (ReservationEntity entity : reservationEntityList) {
            entity.setActive(false);
            reservationRepository.edit(entity);
            emailService.sendCancelMail(entity);
        }
    }

    private List<ReservationEntity> decideToCancelAllOrUntilDate(LocalDateTime dateTime, Long carId) {
        List<ReservationEntity> reservationList;
        if (dateTime.isAfter(LocalDateTime.now())) {
            reservationList = reservationRepository.getByCarAndDate(dateTime, carId);
        } else {
            reservationList = reservationRepository.getByCar(carId);
        }
        return reservationList;
    }
}
