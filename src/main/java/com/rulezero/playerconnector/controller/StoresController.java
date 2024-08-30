package com.rulezero.playerconnector.controller;

import com.rulezero.playerconnector.controller.model.StoresData;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import com.rulezero.playerconnector.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/stores")
@Slf4j
public class StoresController {

    @Autowired
    private StoreService storesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoresData createStore(@RequestBody StoresData storesData) {
        log.info("Requesting Store Creation: {}", storesData);
        return storesService.saveStore(storesData);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<StoresData> partiallyUpdateStore(@PathVariable Long storeId, @RequestBody StoresData storesData) {
        try {
            StoresData updatedStore = storesService.patchStore(storeId, storesData);
            return ResponseEntity.ok(updatedStore);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoresData> getStoreById(@PathVariable Long storeId) {
        try {
            StoresData storeData = storesService.getStoreById(storeId);
            return ResponseEntity.ok(storeData);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/search")
    public List<StoresData> searchStores(@RequestParam String query) {
        log.info("Requesting Store Search: {}", query);
        if (query == null || query.isEmpty()) {
            log.info("You must specify a query");
            return List.of();
        } else {
            return storesService.searchStoresByName(query);
        }
    }

    @DeleteMapping("/{storeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        log.info("Requesting Store Deletion: {}", storeId);
        try {
            storesService.deleteStore(storeId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public List<StoresData> getAllStores() {
        log.info("Requesting All Stores");
        return storesService.getAllStores();
    }

    @PatchMapping("/{storeId}/games")
    public ResponseEntity<Void> updateStoreGames(@PathVariable Long storeId, @RequestBody Set<Long> gameIds) {
        log.info("Updating games for Store {}: {}", storeId, gameIds);
        try {
            StoresData storesData = new StoresData();
            storesData.setStoreGameIds(gameIds);
            storesService.patchStore(storeId, storesData);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{storeId}/users")
    public ResponseEntity<Void> updateStoreUsers(@PathVariable Long storeId, @RequestBody Set<Long> userIds) {
        log.info("Updating users for Store {}: {}", storeId, userIds);
        try {
            StoresData storesData = new StoresData();
            storesData.setStoreUserIds(userIds);
            storesService.patchStore(storeId, storesData);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteStores(@RequestBody List<Long> storeIds) {
        log.info("Requesting Deletion of Stores: {}", storeIds);
        try {
            storesService.deleteStores(storeIds);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}