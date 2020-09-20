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

@RestController
@RequestMapping(path = "/statistics", produces = "application/json")
@CrossOrigin("http://localhost:4200")
public class StatisticsResource {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsModel> getStatistics() {
        return new ResponseEntity<>(statisticsService.getStatistics(), HttpStatus.OK);
    }
}
