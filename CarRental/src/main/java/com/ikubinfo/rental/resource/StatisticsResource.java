package com.ikubinfo.rental.resource;

import com.ikubinfo.rental.model.StatisticsModel;
import com.ikubinfo.rental.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ikubinfo.rental.resource.utils.ApiConstants.CLIENT_APP;
import static com.ikubinfo.rental.resource.utils.ApiConstants.STATISTICS_PATH;

@RestController
@RequestMapping(path = STATISTICS_PATH, produces = "application/json")
@CrossOrigin(CLIENT_APP)
public class StatisticsResource {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsModel> getStatistics() {
        return new ResponseEntity<>(statisticsService.getStatistics(), HttpStatus.OK);
    }
}
