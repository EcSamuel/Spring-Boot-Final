package com.rulezero.playerconnector.handler;

import com.rulezero.playerconnector.controller.model.AvailabilityData;
import com.rulezero.playerconnector.entity.Availability;
import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.service.AvailabilityService;
import com.rulezero.playerconnector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class AvailabilityMenuHandler {

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private UserService userService;

    private Scanner scanner = new Scanner(System.in);

    public void process() {
        List<String> availabilityMenu = List.of(
                "1) Add Availability",
                "2) List Availabilities",
                "3) Select and Update Availability",
                "4) Delete Availability",
                "0) Back"
        );

        boolean back = false;
        while (!back) {
            int selection = getUserSelection(availabilityMenu);
            switch (selection) {
                // TODO: Verify addAvailability
                case 1 -> addAvailability();
                // TODO: Verify listAvailabilities
                case 2 -> listAvailabilities();
                // TODO: Verify selectAndUpdateAvailability
                case 3 -> selectAndUpdateAvailability();
                // TODO: Verify deleteAvailability
                case 4 -> deleteAvailability();
                case 0 -> back = true;
                default -> System.out.println("Invalid selection");
            }
        }
    }

    private int getUserSelection(List<String> menu) {
        System.out.println("\nMake a selection:");
        menu.forEach(System.out::println);
        System.out.print("You selected: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void addAvailability() {
        System.out.println("Enter user Id:");
        Long userId = Long.parseLong(scanner.nextLine());

        System.out.println("Enter day of week:");
        String dayOfWeek = scanner.nextLine();

        System.out.println("Enter start time:");
        String startTime = scanner.nextLine();

        System.out.println("Enter end time:");
        String endTime = scanner.nextLine();

        AvailabilityData newAvailability = new AvailabilityData();
        newAvailability.setDayOfWeek(dayOfWeek);
        newAvailability.setStartTime(startTime);
        newAvailability.setEndTime(endTime);

        try {
            // Convert AvailabilityData to Availability entity
            Availability availability = availabilityService.convertToEntity(newAvailability);

            // Fetch the User entity
            Users user = userService.getUserEntityById(userId);

            // Save the availability
            Availability savedAvailability = availabilityService.saveAvailability(availability, user);

            System.out.println("Availability added: " + availabilityService.mapToData(savedAvailability));
        } catch (Exception e) {
            System.out.println("Error adding availability: " + e.getMessage());
        }
    }
// TODO: saveAvailability might be expecting two things passed in, one having to do with Users of some form. This might be resolved, or the problem is facing elsewhere
    private void listAvailabilities() {
        List<AvailabilityData> availabilities = availabilityService.getAllAvailabilities();
        availabilities.forEach(availability -> System.out.println(availability.getAvailabilityId() + ": " + availability.getDayOfWeek() + " " + availability.getStartTime() + " - " + availability.getEndTime()));
    }

    private void selectAndUpdateAvailability() {
        List<AvailabilityData> availabilities = availabilityService.getAllAvailabilities();
        List<String> availabilityDescriptions = availabilities.stream()
                .map(a -> a.getAvailabilityId() + ": " + a.getDayOfWeek() + " " + a.getStartTime() + " - " + a.getEndTime())
                .collect(Collectors.toList());

        System.out.println("Select an availability to update:");
        for (int i = 0; i < availabilityDescriptions.size(); i++) {
            System.out.println((i + 1) + ") " + availabilityDescriptions.get(i));
        }

        int selection = Integer.parseInt(scanner.nextLine()) - 1;
        if (selection >= 0 && selection < availabilities.size()) {
            AvailabilityData selectedAvailability = availabilities.get(selection);
            updateAvailability(selectedAvailability);
        } else {
            System.out.println("Invalid selection");
        }
    }

    private void updateAvailability(AvailabilityData existingAvailability) {
        System.out.println("Updating availability: " + existingAvailability.getDayOfWeek() + " " + existingAvailability.getStartTime() + " - " + existingAvailability.getEndTime());

        System.out.println("Enter new day of week (leave blank to keep current):");
        String dayOfWeek = scanner.nextLine();
        if (!dayOfWeek.isEmpty()) {
            existingAvailability.setDayOfWeek(dayOfWeek);
        }

        System.out.println("Enter new start time (leave blank to keep current):");
        String startTime = scanner.nextLine();
        if (!startTime.isEmpty()) {
            existingAvailability.setStartTime(startTime);
        }

        System.out.println("Enter new end time (leave blank to keep current):");
        String endTime = scanner.nextLine();
        if (!endTime.isEmpty()) {
            existingAvailability.setEndTime(endTime);
        }

        AvailabilityData updatedAvailability = availabilityService.updateAvailability(existingAvailability);
        System.out.println("Availability updated: " + updatedAvailability);
    }

    private void deleteAvailability() {
        System.out.println("Enter availability ID to delete:");
        Long availabilityId = Long.parseLong(scanner.nextLine());

        availabilityService.deleteAvailabilityById(availabilityId);
        System.out.println("Availability deleted.");
    }
}