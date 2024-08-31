package com.rulezero.playerconnector.handler;

import com.rulezero.playerconnector.controller.model.UsersData;
import com.rulezero.playerconnector.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class UserMenuHandler {

    @Autowired
    private UserService usersService;

    private Scanner scanner = new Scanner(System.in);

    public void process() {
        List<String> userMenu = List.of(
                "1) Add a User",
                "2) List Users",
                "3) Select and Update a User",
                "4) Delete a User",
                "0) Back"
        );

        boolean back = false;
        while (!back) {
            int selection = getUserSelection(userMenu);
            switch (selection) {
                case 1 -> addUser();
                case 2 -> listUsers();
                case 3 -> selectAndUpdateUser();
                case 4 -> deleteUser();
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
        users.forEach(user -> System.out.println(user.getUserId() + ": " + user.getFirstName() + " " + user.getLastName()));
    }

    private void selectAndUpdateUser() {
        List<UsersData> users = usersService.getAllUsers();
        List<String> userNames = users.stream()
                .map(user -> user.getUserId() + ": " + user.getFirstName() + " " + user.getLastName())
                .collect(Collectors.toList());

        System.out.println("Select a user to update:");
        for (int i = 0; i < userNames.size(); i++) {
            System.out.println((i + 1) + ") " + userNames.get(i));
        }

        int selection = Integer.parseInt(scanner.nextLine()) - 1;
        if (selection >= 0 && selection < users.size()) {
            UsersData selectedUser = users.get(selection);
            updateUser(selectedUser);
        } else {
            System.out.println("Invalid selection");
        }
    }

    private void updateUser(UsersData existingUser) {
        System.out.println("Updating user: " + existingUser.getFirstName() + " " + existingUser.getLastName());

        System.out.println("Enter new first name (leave blank to keep current):");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            existingUser.setFirstName(firstName);
        }

        System.out.println("Enter new last name (leave blank to keep current):");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            existingUser.setLastName(lastName);
        }

        // TODO: (add similar update logic for other fields)

        UsersData updatedUser = usersService.patchUser(existingUser.getUserId(), existingUser);
        System.out.println("User updated: " + updatedUser);
    }

    private void deleteUser() {
        System.out.println("Enter user ID to delete:");
        Long userId = Long.parseLong(scanner.nextLine());

        usersService.deleteUser(userId);
        System.out.println("User deleted.");
    }
}
