package com.ikubinfo.rental.service;

import com.ikubinfo.rental.converter.ReservationConverter;
import com.ikubinfo.rental.converter.ReservationPageConverter;
import com.ikubinfo.rental.entity.ReservationEntity;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
import com.ikubinfo.rental.exceptions.messages.NotFound;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.model.Mail;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.ReservationPage;
import com.ikubinfo.rental.repository.ReservationRepository;
import com.ikubinfo.rental.repository.UserRepository;
import com.ikubinfo.rental.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
        if (carName == null) {
            carName = "";
        }
        List<ReservationModel> reservationList = reservationConverter.toModelObject(reservationRepository
                .getByUser(jwtTokenUtil.getUsername(), startIndex, pageSize, carName, startDate2, endDate2));
        Long totalRecords = reservationRepository.countNumberOfReservationsByUsername(jwtTokenUtil.getUsername(),
                carName, startDate2, endDate2);
        return reservationPageConverter.toModel(reservationList, totalRecords);
    }

    public HashMap<String, LocalDateTime> getFilterData(String startDate, String endDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        HashMap<String, LocalDateTime> dataMap = new HashMap<String, LocalDateTime>();
        LocalDateTime startDate2;
        LocalDateTime endDate2;
        if (startDate != null) {
            startDate2 = LocalDateTime.parse(startDate, dateFormatter);
        } else {
            startDate2 = LocalDateTime.parse("1900-01-01 00:00:00", dateFormatter);
        }
        dataMap.put("startDate", startDate2);
        if (endDate != null) {
            endDate2 = LocalDateTime.parse(endDate, dateFormatter);
        } else {
            endDate2 = LocalDateTime.parse("2900-01-01 00:00:00", dateFormatter);
        }
        dataMap.put("endDate", endDate2);
        return dataMap;
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
        checkDates(model.getStartDate(), model.getEndDate());
        ReservationEntity entity = reservationConverter.toEntity(getById(id));
        checkIfUpdateIsAvailable(entity.getCarId(), model.getStartDate(), model.getEndDate(),
                entity.getId());
        entity.setCreated_at(Calendar.getInstance());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        reservationRepository.edit(entity);
        sendNotificationMail(entity, model.getFee());
    }

    private void sendNotificationMail(ReservationEntity reservationEntity, double fee) {
        Mail mail = emailService.setMailProperties(reservationEntity, fee);
        CarModel carModel = carService.getById(reservationEntity.getCarId());
        emailService.sendConfirmationMail(mail, carModel);
    }

    public void cancel(Long id) {
        ReservationEntity entity = reservationConverter.toEntity(getById(id));
        entity.setActive(false);
        reservationRepository.edit(entity);
    }

    private void checkIfUpdateIsAvailable(Long carId, LocalDateTime startDate, LocalDateTime endDate, Long id) {
        Long reservations = reservationRepository.updateIfAvailable(carId, startDate, endDate, id);
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

    public int cancelByCarAndDate(LocalDateTime date, Long carId) {
        List<ReservationEntity> reservationList;

        if (date.isAfter(LocalDateTime.now())) {
            reservationList = reservationRepository.getByCarAndDate(date, carId);
        } else {
            reservationList = reservationRepository.getByCar(carId);
        }

        for (ReservationEntity entity : reservationList) {
            entity.setActive(false);
            reservationRepository.edit(entity);
            emailService.sendCancelMail(emailService.setCancelMailProperties(entity));
        }
        return reservationList.size();
    }
}
