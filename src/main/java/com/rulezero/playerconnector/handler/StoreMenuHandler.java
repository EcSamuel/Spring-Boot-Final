package com.rulezero.playerconnector.handler;

import com.rulezero.playerconnector.controller.model.StoresData;
import com.rulezero.playerconnector.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreMenuHandler {

    @Autowired
    private StoreService storesService;

    private Scanner scanner = new Scanner(System.in);

    public void process() {
        List<String> storeMenu = List.of(
                "1) Add a Store",
                "2) List Stores",
                "3) Select a Store",
                "4) Update a Store",
                "5) Delete a Store",
                "0) Back"
        );

        boolean back = false;
        while (!back) {
            int selection = getUserSelection(storeMenu);
            switch (selection) {
                case 1 -> addStore();
                case 2 -> listStores();
                case 3 -> selectStore();
                case 4 -> patchStore();
                case 5 -> deleteStore();
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

    private void addStore() {
        System.out.println("Enter store name:");
        String storeName = scanner.nextLine();

        System.out.println("Enter store address:");
        String storeAddress = scanner.nextLine();

        System.out.println("Enter store city:");
        String storeCity = scanner.nextLine();

        System.out.println("Enter store phone:");
        String storePhone = scanner.nextLine();

        System.out.println("Enter store email:");
        String storeEmail = scanner.nextLine();

        System.out.println("Does the store have disability access? (true/false):");
        Boolean storeDisabilityCheck = Boolean.parseBoolean(scanner.nextLine());

        System.out.println("Does the store allow outside food? (true/false):");
        Boolean outsideFood = Boolean.parseBoolean(scanner.nextLine());

        System.out.println("Enter store region:");
        String storeRegion = scanner.nextLine();

        System.out.println("Enter store parking capacity:");
        Integer storeParking = Integer.parseInt(scanner.nextLine());

        StoresData newStore = new StoresData();
        newStore.setStoreName(storeName);
        newStore.setStoreAddress(storeAddress);
        newStore.setStoreCity(storeCity);
        newStore.setStorePhone(storePhone);
        newStore.setStoreEmail(storeEmail);
        newStore.setStoreDisabilityCheck(storeDisabilityCheck);
        newStore.setOutsideFood(outsideFood);
        newStore.setStoreRegion(storeRegion);
        newStore.setStoreParking(storeParking);
        newStore.setStoreGameIds(null);
        newStore.setStoreUserIds(null);

        StoresData savedStore = storesService.saveStore(newStore);
        System.out.println("Store added: " + savedStore);
    }

    private void listStores() {
        List<StoresData> stores = storesService.getAllStores();
        stores.forEach(store -> System.out.println(store.toString()));
    }

    private void patchStore() {
        System.out.println("Enter store ID to update:");
        Long storeId = Long.parseLong(scanner.nextLine());

        System.out.println("Enter new store name:");
        String storeName = scanner.nextLine();

        System.out.println("Enter new store address:");
        String storeAddress = scanner.nextLine();

        System.out.println("Enter new store city:");
        String storeCity = scanner.nextLine();

        System.out.println("Enter new store phone:");
        String storePhone = scanner.nextLine();

        System.out.println("Enter new store email:");
        String storeEmail = scanner.nextLine();

        System.out.println("Enter new store region:");
        String storeRegion = scanner.nextLine();

        System.out.println("Enter new store parking capacity:");
        Integer storeParking = Integer.parseInt(scanner.nextLine());

        // Fetch the existing store, update it, and save it
        StoresData existingStore = storesService.getStoreById(storeId);
        existingStore.setStoreName(storeName);
        existingStore.setStoreAddress(storeAddress);
        existingStore.setStoreCity(storeCity);
        existingStore.setStorePhone(storePhone);
        existingStore.setStoreEmail(storeEmail);
        existingStore.setStoreRegion(storeRegion);
        existingStore.setStoreParking(storeParking);
        // Updating relationships as needed
        existingStore.setStoreGameIds(null);
        existingStore.setStoreUserIds(null);

        StoresData updatedStore = storesService.patchStore(existingStore);
        System.out.println("Store updated: " + updatedStore);
    }

    private void selectStore() {
        System.out.println("Enter store ID:");
        Long storeId = Long.parseLong(scanner.nextLine());

        StoresData store = storesService.getStoreById(storeId);
        System.out.println("Selected store: " + store);
    }

    private void deleteStore() {
        System.out.println("Enter store ID to delete:");
        Long storeId = Long.parseLong(scanner.nextLine());

        storesService.deleteStore(storeId);
        System.out.println("Store deleted.");
    }
}
