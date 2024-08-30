package com.rulezero.playerconnector.handler;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AvailabilityMenuHandler {
    public void process() {
        List<String> availabilityMenu = List.of(
                "1) Add Availability",
                "2) List Availability",
                "3) Select Availability",
                "4) Update Availability",
                "5) Delete Availability",
                "0) Back"
        );
    }
}
