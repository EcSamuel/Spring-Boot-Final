package com.rulezero.playerconnector.controller.model;

import lombok.Data;

@Data
public class AvailabilityData {
    private Long availabilityId;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private Long userId; // Foreign key reference
}
