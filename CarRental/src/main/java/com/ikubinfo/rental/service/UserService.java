package com.ikubinfo.rental.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.config.NonValidDataException;
import com.ikubinfo.rental.config.UserAlreadyExistsException;
import com.ikubinfo.rental.converter.UserConverter;
import com.ikubinfo.rental.entity.UserEntity;
import com.ikubinfo.rental.model.ReservationModel;
import com.ikubinfo.rental.model.UserModel;
import com.ikubinfo.rental.model.UserPage;
import com.ikubinfo.rental.repository.UserRepository;
import com.ikubinfo.rental.security.JwtTokenUtil;

@Service
public class UserService {

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

	private static Logger logger = LogManager.getLogger(UserService.class);

	public UserService() {

	}

	public UserModel getById(Long id) {
		try {
			logger.info("Getting user by id.");
			return userConverter.toModel(userRepository.getById(id));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}

	public UserPage getAll(int startIndex, int pageSize, String name) {
		logger.info("Getting all users.");
		if (name == null) {
			name = "";
		}
		List<UserModel> userList = userConverter.toModel(userRepository.getAll(startIndex, pageSize, name));
		Long totalRecords = userRepository.countActiveUsers(name);
		UserPage userPage = new UserPage();
		userPage.setTotalRecords(totalRecords);
		userPage.setUserList(userList);
		return userPage;
	}

	public UserModel getByUsername(String username) {
		try {
			logger.info("Getting user by username.");
			return userConverter.toModel(userRepository.getByUsername(username));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}

	public void register(UserModel user) {
		try {
			validateUserData(user);
			logger.info("Registering new user.");
			if (user.getUsername().trim().length() > 0) {
				checkIfExists(user.getUsername());
				UserEntity entity = userConverter.toEntity(user);
				entity.setPassword(passwordEncoder.encode(user.getPassword()));
				entity.setActive(true);
				entity.setRoleId(2);
				userRepository.save(entity);
			} else {
				throw new NonValidDataException("Username is required.");
			}
		} catch (UserAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");
		} catch (NonValidDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public void edit(UserModel user) {
		try {
			UserEntity entity = new UserEntity();
			UserEntity loggedUser = userRepository.getByUsername(jwtTokenUtil.getUsername());
			if (user.getPassword() == null) {
				validateUserData(user);
				entity = userConverter.toEntity(user);
				entity.setPassword(loggedUser.getPassword());
				entity.setId(loggedUser.getId());
				entity.setActive(true);
				entity.setRoleId(2);
				entity.setUsername(loggedUser.getUsername());
				userRepository.edit(entity);
			} else {
				loggedUser.setPassword(passwordEncoder.encode(user.getPassword().trim()));
				userRepository.edit(loggedUser);
			}
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		} catch (NonValidDataException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public void delete() {
		try {
			logger.info("Deleting user.");
			UserEntity entity = userRepository.getByUsername(jwtTokenUtil.getUsername());
			entity.setActive(false);
			List<ReservationModel> resList = reservationService.getByUsername();
			for (ReservationModel res : resList) {
				res.setActive(false);
				reservationService.cancel(res.getId());
			}
			userRepository.edit(entity);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}

	public void checkIfExists(String username) throws UserAlreadyExistsException {
		try {
			logger.info("Checking if user already exists.");
			userRepository.getByUsername(username);
			throw new UserAlreadyExistsException("User already exists.");
		} catch (NoResultException e) {
			logger.info("User is able to register.");
		}
	}

	public void validateUserData(UserModel model) throws NonValidDataException {
		try {
			if (model.getFirstName().trim().length() == 0) {
				throw new NonValidDataException("First name is required.");
			}
			if (model.getLastName().trim().length() == 0) {
				throw new NonValidDataException("Last name is required.");
			}
			if (model.getBirthdate() == null) {
				throw new NonValidDataException("Birthdate is required.");
			} else if (!isOver18(model.getBirthdate())) {
				throw new NonValidDataException("You must be over 18 years old.");
			}
			if (model.getAddress().trim().length() == 0) {
				throw new NonValidDataException("Address is required.");
			}
			if (model.getPhone().trim().length() == 0) {
				throw new NonValidDataException("Phone is required.");
			}
			if (model.getEmail().trim().length() == 0) {
				throw new NonValidDataException("Email is required.");
			}
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User data are missing.");
		}
	}

	public boolean isOver18(LocalDateTime birthdate) {
		if (LocalDateTime.now().getYear() - birthdate.getYear() >= 18) {
			return true;
		} else {
			return false;
		}
	}
}
