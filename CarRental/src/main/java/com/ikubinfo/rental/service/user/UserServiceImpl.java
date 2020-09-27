package com.ikubinfo.rental.service.user;

import com.ikubinfo.rental.service.authorization.AuthorizationService;
import com.ikubinfo.rental.service.exceptions.CarRentalBadRequestException;
import com.ikubinfo.rental.service.exceptions.CarRentalNotFoundException;
import com.ikubinfo.rental.service.exceptions.messages.BadRequest;
import com.ikubinfo.rental.service.exceptions.messages.NotFound;
import com.ikubinfo.rental.service.reservation.ReservationService;
import com.ikubinfo.rental.service.reservation.dto.ReservationModel;
import com.ikubinfo.rental.service.user.converter.UserConverter;
import com.ikubinfo.rental.service.user.dto.UserEntity;
import com.ikubinfo.rental.service.user.dto.UserFilter;
import com.ikubinfo.rental.service.user.dto.UserModel;
import com.ikubinfo.rental.service.user.dto.UserPage;
import com.ikubinfo.rental.service.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ReservationService reservationService;

    @Override
    public UserModel getById(Long id) {
        try {
            LOGGER.info("Getting user with id {}", id);
            return userConverter.toModel(userRepository.getById(id));
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @Override
    public UserPage getAll(UserFilter userFilter) {
        List<UserModel> userList = userConverter.toModel(userRepository.getAll(userFilter));
        Long totalRecords = userRepository.countActiveUsers(userFilter.getName());
        UserPage userPage = new UserPage();
        userPage.setTotalRecords(totalRecords);
        userPage.setUserList(userList);
        return userPage;
    }

    @Override
    public UserModel getByUsername(String username) {
        try {
            LOGGER.info("Getting user with username {}", username);
            return userConverter.toModel(userRepository.getByUsername(username));
        } catch (NoResultException e) {
            throw new CarRentalNotFoundException(NotFound.USER_NOT_FOUND.getErrorMessage());
        }
    }

    @Override
    public void register(UserModel user) {
        validateUserData(user);
        checkIfExists(user.getUsername());
        UserEntity entity = userConverter.toEntity(user);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setActive(true);
        entity.setRoleId(2);
        userRepository.save(entity);
    }

    @Override
    public void editProfile(UserModel user) {
        UserEntity loggedUser = userConverter.toEntity(getByUsername(authorizationService.getCurrentLoggedUserUsername()));
        updateOtherDataExceptPassword(user, loggedUser);
    }

    @Override
    public void editPassword(UserModel user) {
        UserEntity loggedUser = userConverter.toEntity(getByUsername(authorizationService.getCurrentLoggedUserUsername()));
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

    @Override
    public void delete() {
        LOGGER.debug("Deleting user with username {}", authorizationService.getCurrentLoggedUserUsername());
        UserEntity entity = userConverter.toEntity(getByUsername(authorizationService.getCurrentLoggedUserUsername()));
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
