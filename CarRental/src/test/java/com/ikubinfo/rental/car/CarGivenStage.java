package com.ikubinfo.rental.car;

import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.model.CarModel;
import com.ikubinfo.rental.security.JwtTokenUtil;
import com.ikubinfo.rental.service.CarService;
import com.ikubinfo.rental.util.TokenCreator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ikubinfo.rental.car.util.CarUtils.createCarModelWithStatus;
import static com.ikubinfo.rental.util.CommonUtils.createMultipartFile;

@JGivenStage
public class CarGivenStage extends Stage<CarGivenStage> {

    @Autowired
    private TokenCreator tokenCreator;

    @Autowired
    private CarService carService;

    @ProvidedScenarioState
    private CarModel savedCarModel;

    public CarGivenStage user_is_logged_in_as_admin() {
       tokenCreator.createAdminToken();
       return self();
    }

    public CarGivenStage admin_saves_new_car() {
        user_is_logged_in_as_admin();
        savedCarModel = carService.save(createCarModelWithStatus(StatusEnum.AVAILABLE), createMultipartFile());
        return self();
    }


}
