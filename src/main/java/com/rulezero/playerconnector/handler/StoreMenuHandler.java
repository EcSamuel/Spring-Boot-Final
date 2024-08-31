package com.rulezero.playerconnector.handler;

import com.rulezero.playerconnector.controller.model.StoresData;
import com.rulezero.playerconnector.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreMenuHandler {

    @Autowired
    private StoreService storesService;

    private Scanner scanner = new Scanner(System.in);

    public void process() {
        List<String> storeMenu = List.of(
                "1) Add a Store",
                "2) List Stores",
                "3) Select and Update a Store",
                "4) Delete a Store",
                "0) Back"
        );

        boolean back = false;
        while (!back) {
            int selection = getUserSelection(storeMenu);
            switch (selection) {
                case 1 -> addStore();
                case 2 -> listStores();
                case 3 -> selectAndUpdateStore();
                case 4 -> deleteStore();
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
        stores.forEach(store -> System.out.println(store.getStoreId() + ": " + store.getStoreName()));
    }

    private void selectAndUpdateStore() {
        List<StoresData> stores = storesService.getAllStores();
        List<String> storeNames = stores.stream()
                .map(store -> store.getStoreId() + ": " + store.getStoreName())
                .collect(Collectors.toList());

        System.out.println("Select a store to update:");
        for (int i = 0; i < storeNames.size(); i++) {
            System.out.println((i + 1) + ") " + storeNames.get(i));
        }

        int selection = Integer.parseInt(scanner.nextLine()) - 1;
        if (selection >= 0 && selection < stores.size()) {
            StoresData selectedStore = stores.get(selection);
            updateStore(selectedStore);
        } else {
            System.out.println("Invalid selection");
        }
    }

    private void updateStore(StoresData existingStore) {
        System.out.println("Updating store: " + existingStore.getStoreName());

        System.out.println("Enter new store name (leave blank to keep current):");
        String storeName = scanner.nextLine();
        if (!storeName.isEmpty()) {
            existingStore.setStoreName(storeName);
        }

        // TODO: (add similar update logic for other fields)

        StoresData updatedStore = storesService.patchStore(existingStore.getStoreId(), existingStore);
        System.out.println("Store updated: " + updatedStore);
    }

    private void deleteStore() {
        System.out.println("Enter store ID to delete:");
        Long storeId = Long.parseLong(scanner.nextLine());

        storesService.deleteStore(storeId);
        System.out.println("Store deleted.");
    }
}
