package com.ikubinfo.rental.service;

import com.ikubinfo.rental.converter.UserConverter;
import com.ikubinfo.rental.entity.UserEntity;
import com.ikubinfo.rental.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.exceptions.messages.BadRequest;
import com.ikubinfo.rental.exceptions.messages.NotFound;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.model.page.UserPage;
import com.ikubinfo.rental.repository.UserRepository;
import com.ikubinfo.rental.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ReservationService reservationService;

    public UserModel getById(Long id) {
        try {
            LOGGER.info("Getting user with id {}", id);
            return userConverter.toModel(userRepository.getById(id));
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.USER_NOT_FOUND.getErrorMessage());
        }
    }

    public UserPage getAll(int startIndex, int pageSize, String name) {
        List<UserModel> userList = userConverter.toModel(userRepository.getAll(startIndex, pageSize, name));
        Long totalRecords = userRepository.countActiveUsers(name);
        UserPage userPage = new UserPage();
        userPage.setTotalRecords(totalRecords);
        userPage.setUserList(userList);
        return userPage;
    }

    public UserModel getByUsername(String username) {
        try {
            LOGGER.info("Getting user with username {}", username);
            return userConverter.toModel(userRepository.getByUsername(username));
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.USER_NOT_FOUND.getErrorMessage());
        }
    }

    public void register(UserModel user) {
        validateUserData(user);
        checkIfExists(user.getUsername());
        UserEntity entity = userConverter.toEntity(user);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setActive(true);
        entity.setRoleId(2);
        userRepository.save(entity);
    }

    public void editProfile(UserModel user) {
        UserEntity loggedUser = userConverter.toEntity(getByUsername(jwtTokenUtil.getUsername()));
        updateOtherDataExceptPassword(user, loggedUser);
    }

    public void editPassword(UserModel user) {
        UserEntity loggedUser = userConverter.toEntity(getByUsername(jwtTokenUtil.getUsername()));
        loggedUser.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        userRepository.edit(loggedUser);
    }

    private void updateOtherDataExceptPassword(UserModel user, UserEntity loggedUser) {
        validateUserData(user);
        UserEntity entity = userConverter.toEntity(user);
        entity.setPassword(loggedUser.getPassword());
        entity.setId(loggedUser.getId());
        entity.setActive(true);
        entity.setRoleId(2);
        entity.setUsername(loggedUser.getUsername());
        userRepository.edit(entity);
    }

    public void delete() {
        LOGGER.debug("Deleting user with username {}", jwtTokenUtil.getUsername());
        UserEntity entity = userConverter.toEntity(getByUsername(jwtTokenUtil.getUsername()));
        entity.setActive(false);
        cancelAllReservationsOfDeletedUser();
        userRepository.edit(entity);
    }

    private void cancelAllReservationsOfDeletedUser() {
        List<ReservationModel> resList = reservationService.getByUsername();
        for (ReservationModel res : resList) {
            res.setActive(false);
            reservationService.cancel(res.getId());
        }
    }

    private void checkIfExists(String username) {
        try {
            LOGGER.debug("Checking if user with username  {} already exists", username);
            userRepository.getByUsername(username);
            throw new CarRentalBadRequestException(BadRequest.USER_ALREADY_EXISTS.getErrorMessage());
        } catch (NoResultException e) {
            LOGGER.debug("User with username {} is able to register.", username);
        }
    }

    private void validateUserData(UserModel model) {
        try {
            if (model.getFirstName().trim().length() == 0) {
                throw new CarRentalBadRequestException(BadRequest.FIRSTNAME_REQUIRED.getErrorMessage());
            }
            if (model.getLastName().trim().length() == 0) {
                throw new CarRentalBadRequestException(BadRequest.LASTNAME_REQUIRED.getErrorMessage());
            }
            if (model.getUsername().trim().length() < 5) {
                throw new CarRentalBadRequestException(BadRequest.USERNAME_NOT_VALID.getErrorMessage());
            }
            if (model.getBirthdate() == null) {
                throw new CarRentalBadRequestException(BadRequest.BIRTHDATE_REQUIRED.getErrorMessage());
            } else if (!isOver18(model.getBirthdate())) {
                throw new CarRentalBadRequestException(BadRequest.MUST_BE_OVER_18.getErrorMessage());
            }
            if (model.getAddress().trim().length() == 0) {
                throw new CarRentalBadRequestException(BadRequest.ADDRESS_REQUIRED.getErrorMessage());
            }
            if (model.getPhone().trim().length() == 0) {
                throw new CarRentalBadRequestException(BadRequest.PHONE_REQUIRED.getErrorMessage());
            }
            if (model.getEmail().trim().length() == 0) {
                throw new CarRentalBadRequestException(BadRequest.EMAIL_REQUIRED.getErrorMessage());
            }
        } catch (NullPointerException e) {
            throw new CarRentalBadRequestException(BadRequest.USER_DATA_MISSING.getErrorMessage());
        }
    }

    private boolean isOver18(LocalDateTime birthdate) {
        return LocalDateTime.now().getYear() - birthdate.getYear() >= 18;
    }
}
