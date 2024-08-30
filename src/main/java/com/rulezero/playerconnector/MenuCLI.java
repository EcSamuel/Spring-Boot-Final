package com.rulezero.playerconnector;

import com.rulezero.playerconnector.controller.model.GamesData;
import com.rulezero.playerconnector.handler.AvailabilityMenuHandler;
import com.rulezero.playerconnector.handler.GameMenuHandler;
import com.rulezero.playerconnector.handler.StoreMenuHandler;
import com.rulezero.playerconnector.handler.UserMenuHandler;
import com.rulezero.playerconnector.service.AvailabilityService;
import com.rulezero.playerconnector.service.GamesService;
import com.rulezero.playerconnector.service.StoreService;
import com.rulezero.playerconnector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class MenuCLI {

    @Autowired
    private GamesService gamesService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @Autowired
    AvailabilityService availabilityService;

    @Autowired
    private AvailabilityMenuHandler availabilityMenuHandler;

    @Autowired
    private UserMenuHandler userMenuHandler;

    @Autowired
    private StoreMenuHandler storeMenuHandler;

    @Autowired
    private GameMenuHandler gameMenuHandler;

    private List<String> mainMenu = List.of(
            "1) Users",
            "2) Games",
            "3) Availability",
            "4) Stores",
            "0) Quit"
    );

    private Scanner scanner = new Scanner(System.in);

    public void processUserSelections() {
        boolean done = false;

        while (!done) {
            int selection = getUserSelection(mainMenu);
            switch (selection) {
                case 1 -> userMenuHandler.process();
                case 2 -> gameMenuHandler.process();
                case 3 -> availabilityMenuHandler.process();
                case 4 -> storeMenuHandler.process();
                case 0 -> done = true;
                default -> System.out.println("Invalid selection");
            }
        }
        System.out.println("Exiting the application.");
    }


    private void userMenu() {
        List<String> userMenu = List.of(
                "1) Add a User",
                "2) List Users",
                "3) Select a User",
                "4) Update a User",
                "5) Delete a User",
                "6) Quit"
        );
        processSubMenu(userMenu, "User");
    }

    private void gameMenu() {

        processSubMenu(gameMenu, "Game");
    }

    private void availabilityMenu() {
        List<String> availabilityMenu = List.of(
                "1) Add Availability",
                "2) List Availability",
                "3) Select Availability",
                "4) Update Availability",
                "5) Delete Availability",
                "0) Back"
        );
        processSubMenu(availabilityMenu, "Availability");
    }

    private void storeMenu() {
        List<String> storeMenu = List.of(
                "1) Add a Store",
                "2) List Stores",
                "3) Select a Store",
                "4) Update a Store",
                "5) Delete a Store",
                "0) Back"
        );
        processSubMenu(storeMenu, "Store");
    }

    private int getUserSelection(List<String> menu) {
        System.out.println("\nMake a selection:");
        menu.forEach(System.out::println);
        System.out.println("You selected ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void processSubMenu(List<String> menu, String entityType) {
        boolean back = false;
        while (!back) {
            int selection = getUserSelection(menu);
            switch (selection) {
                case 1 -> System.out.printf("Adding %s...\n", entityType);
                case 2 -> System.out.printf("Listing %ss...\n", entityType);
                case 3 -> System.out.printf("Selecting %s...\n", entityType);
                case 4 -> System.out.printf("Updating %s...\n", entityType);
                case 5 -> System.out.printf("Deleting %s...\n", entityType);
                case 0 -> back = true;
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
    }
}
