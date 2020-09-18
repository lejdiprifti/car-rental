package com.ikubinfo.rental.car;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.service.CarService;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.car.util.CarUtils.createCarModelWithStatus;
import static com.ikubinfo.rental.car.util.CarUtils.createMultipartFile;

@JGivenStage
public class CarWhenStage extends Stage<CarWhenStage> {

    @Autowired
    private CarService carService;

    @ExpectedScenarioState
    private CarEntity carEntity;

    public CarWhenStage admin_tries_to_add_new_car() {
        CarModel carModel = createCarModelWithStatus(StatusEnum.AVAILABLE);
        carService.save(carModel, createMultipartFile());
        return self();
    }

    public CarWhenStage admin_tries_to_update_car() {
        CarModel toUpdateCarModel = createCarModelWithStatus(StatusEnum.SERVIS);
        carService.edit(toUpdateCarModel, createMultipartFile(), carEntity.getId());
        return self();
    }

    public CarWhenStage admin_tries_to_delete_car() {
        carService.delete(carEntity.getId());
        return self();
    }
}
