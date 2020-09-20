package com.ikubinfo.rental.car.util;

import com.ikubinfo.rental.model.enums.StatusEnum;
import com.ikubinfo.rental.model.CarModel;

public class CarUtils {

    public static final Long NON_EXISTING_CAR_ID = 0L;
    public static final Long NON_EXISTING_CATEGORY_ID = 0L;

    private CarUtils() {
    }

    public static CarModel createCarModelWithStatus(StatusEnum statusEnum) {
        CarModel carModel = new CarModel();
        carModel.setName("some name");
        carModel.setPlate("00000");
        carModel.setActive(true);
        carModel.setPhoto(new byte[0]);
        carModel.setDiesel("some diesel");
        carModel.setPrice(10.0);
        carModel.setAvailability(statusEnum);
        carModel.setType("some type");
        carModel.setYear(2020);
        carModel.setDescription("some description");
        return carModel;
    }
}
