package com.rulezero.playerconnector.controller;

import com.rulezero.playerconnector.controller.model.AvailabilityData;
import com.rulezero.playerconnector.service.AvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availabilities")
@Slf4j
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvailabilityData createAvailability(@RequestBody AvailabilityData availabilityData) {
        log.info("Requesting Availability Creation: {}", availabilityData);
        return availabilityService.saveAvailability(availabilityData);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AvailabilityData>> getAvailabilitiesByUserId(@PathVariable Long userId) {
        List<AvailabilityData> availabilities = availabilityService.getAvailabilityByUserId(userId);
        return ResponseEntity.ok(availabilities);
    }
}
