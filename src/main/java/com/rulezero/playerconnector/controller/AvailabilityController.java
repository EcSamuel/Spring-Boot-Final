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

    @GetMapping
    public ResponseEntity<List<AvailabilityData>> getAllAvailabilities() {
        List<AvailabilityData> availabilities = availabilityService.getAllAvailabilities();
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/{availabilityId}")
    public ResponseEntity<AvailabilityData> getAvailabilityById(@PathVariable Long availabilityId) {
        AvailabilityData availability = availabilityService.getAvailabilityById(availabilityId);
        return ResponseEntity.ok(availability);
    }

    @PutMapping("/{availabilityId}")
    public ResponseEntity<AvailabilityData> updateAvailability(
            @PathVariable Long availabilityId,
            @RequestBody AvailabilityData availabilityData) {
        AvailabilityData updatedAvailability = availabilityService.updateAvailability(availabilityId, availabilityData);
        return ResponseEntity.ok(updatedAvailability);
    }

    @DeleteMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailabilityByUserId(@PathVariable Long userId) {
        availabilityService.deleteAvailabilityByUserId(userId);
    }

    @DeleteMapping("/{availabilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailabilityById(@PathVariable Long availabilityId) {
        availabilityService.deleteAvailabilityById(availabilityId);
    }
}
