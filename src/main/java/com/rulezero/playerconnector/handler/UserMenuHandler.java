package com.rulezero.playerconnector.handler;

import com.rulezero.playerconnector.controller.model.UsersData;
import com.rulezero.playerconnector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class UserMenuHandler {

    @Autowired
    private UserService usersService;

    private Scanner scanner = new Scanner(System.in);

    public void process() {
        List<String> userMenu = List.of(
                "1) Add a User",
                "2) List Users",
                "3) Select a User",
                "4) Update a User",
                "5) Delete a User",
                "0) Back"
        );

        boolean back = false;
        while (!back) {
            int selection = getUserSelection(userMenu);
            switch (selection) {
                case 1 -> addUser();
                case 2 -> listUsers();
                case 3 -> selectUser();
                case 4 -> patchUser();
                case 5 -> deleteUser();
                case 0 -> back = true;
                default -> System.out.println("Invalid selection");
            }
        }
    }

    private int getUserSelection(List<String> menu) {
        System.out.println("\nMake a selection:");
        menu.forEach(System.out::println);
        System.out.println("You selected ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void addUser() {
        System.out.println("Enter first name:");
        String firstName = scanner.nextLine();

        System.out.println("Enter last name:");
        String lastName = scanner.nextLine();

        System.out.println("Enter phone number:");
        String userPhone = scanner.nextLine();

        System.out.println("Enter address:");
        String userAddress = scanner.nextLine();

        System.out.println("Enter city:");
        String userCity = scanner.nextLine();

        System.out.println("Enter region:");
        String userRegion = scanner.nextLine();

        System.out.println("Enter login name:");
        String userLoginName = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        System.out.println("Enter email:");
        String userEmail = scanner.nextLine();

        System.out.println("Enter availability ID:");
        Long availabilityId = Long.parseLong(scanner.nextLine());

        UsersData newUser = new UsersData();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUserPhone(userPhone);
        newUser.setUserAddress(userAddress);
        newUser.setUserCity(userCity);
        newUser.setUserRegion(userRegion);
        newUser.setUserLoginName(userLoginName);
        newUser.setPassword(password);
        newUser.setUserEmail(userEmail);
        newUser.setAvailabilityId(availabilityId);
        newUser.setGameIds(null);
        newUser.setStoreIds(null);

        UsersData savedUser = usersService.saveUser(newUser);
        System.out.println("User added: " + savedUser);
    }

    private void listUsers() {
        List<UsersData> users = usersService.getAllUsers();
        users.forEach(user -> System.out.println(user.toString()));
    }

    private void patchUser() {
        System.out.println("Enter user ID to update:");
        Long userId = Long.parseLong(scanner.nextLine());

        System.out.println("Enter new first name:");
        String firstName = scanner.nextLine();

        System.out.println("Enter new last name:");
        String lastName = scanner.nextLine();

        System.out.println("Enter new phone number:");
        String userPhone = scanner.nextLine();

        System.out.println("Enter new address:");
        String userAddress = scanner.nextLine();

        System.out.println("Enter new city:");
        String userCity = scanner.nextLine();

        System.out.println("Enter new region:");
        String userRegion = scanner.nextLine();

        System.out.println("Enter new login name:");
        String userLoginName = scanner.nextLine();

        System.out.println("Enter new password:");
        String password = scanner.nextLine();

        System.out.println("Enter new email:");
        String userEmail = scanner.nextLine();

        System.out.println("Enter new availability ID:");
        Long availabilityId = Long.parseLong(scanner.nextLine());

        // Fetch the existing user, update it, and save it
        UsersData existingUser = usersService.getUserById(userId);
        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setUserPhone(userPhone);
        existingUser.setUserAddress(userAddress);
        existingUser.setUserCity(userCity);
        existingUser.setUserRegion(userRegion);
        existingUser.setUserLoginName(userLoginName);
        existingUser.setPassword(password);
        existingUser.setUserEmail(userEmail);
        existingUser.setAvailabilityId(availabilityId);
        // Updating relationships as needed
        existingUser.setGameIds(null);
        existingUser.setStoreIds(null);

        UsersData updatedUser = usersService.patchUser(existingUser);
        System.out.println("User updated: " + updatedUser);
    }

    private void selectUser() {
        System.out.println("Enter user ID:");
        Long userId = Long.parseLong(scanner.nextLine());

        UsersData user = usersService.getUserById(userId);
        System.out.println("Selected user: " + user);
    }

    private void deleteUser() {
        System.out.println("Enter user ID to delete:");
        Long userId = Long.parseLong(scanner.nextLine());

        usersService.deleteUser(userId);
        System.out.println("User deleted.");
    }
}
