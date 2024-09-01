package com.rulezero.playerconnector.service;

import com.rulezero.playerconnector.controller.model.StoresData;
import com.rulezero.playerconnector.dao.StoresDao;
import com.rulezero.playerconnector.dao.GamesDao;
import com.rulezero.playerconnector.dao.UsersDao;
import com.rulezero.playerconnector.entity.Stores;
import com.rulezero.playerconnector.entity.Games;
import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoresDao storesDao;

    @Autowired
    private GamesDao gamesDao;

    @Autowired
    private UsersDao usersDao;

    @Transactional
    public StoresData saveStore(StoresData storesData) {
        Stores store = convertToEntity(storesData);
        Stores savedStore = storesDao.save(store);
        return convertToStoresData(savedStore);
    }

    private Stores convertToEntity(StoresData storesData) {
        Stores store = new Stores();
        updateStoreFields(store, storesData);
        return store;
    }

    @Transactional
    public StoresData patchStore(Long storeId, StoresData storesData) throws ResourceNotFoundException {
        Stores existingStore = storesDao.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));

        updateStoreFields(existingStore, storesData);

        Stores updatedStore = storesDao.save(existingStore);
        return convertToStoresData(updatedStore);
    }

    private void updateStoreFields(Stores store, StoresData storesData) {
        if (storesData.getStoreName() != null) {
            store.setStoreName(storesData.getStoreName());
        }
        if (storesData.getStoreDisabilityCheck() != null) {
            store.setStoreDisabilityCheck(storesData.getStoreDisabilityCheck());
        }
        if (storesData.getStoreCity() != null) {
            store.setStoreCity(storesData.getStoreCity());
        }
        if (storesData.getStoreRegion() != null) {
            store.setStoreRegion(storesData.getStoreRegion());
        }
        if (storesData.getOutsideFood() != null) {
            store.setOutsideFood(storesData.getOutsideFood());
        }
        if (storesData.getStoreAddress() != null) {
            store.setStoreAddress(storesData.getStoreAddress());
        }
        if (storesData.getStorePhone() != null) {
            store.setStorePhone(storesData.getStorePhone());
        }
        if (storesData.getStoreEmail() != null) {
            store.setStoreEmail(storesData.getStoreEmail());
        }
        if (storesData.getStoreParking() != null) {
            store.setStoreParking(storesData.getStoreParking());
        }
        if (storesData.getStoreGameIds() != null) {
            updateStoreGames(store, storesData.getStoreGameIds());
        }
        if (storesData.getStoreUserIds() != null) {
            updateStoreUsers(store, storesData.getStoreUserIds());
        }
    }

    @Transactional
    public void updateStoreGames(Stores store, Set<Long> gameIds) {
        Set<Games> newGames = gameIds.stream()
                .map(gameId -> gamesDao.findById(gameId)
                        .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId)))
                .collect(Collectors.toSet());

        store.getStoreGames().clear();
        store.getStoreGames().addAll(newGames);
        newGames.forEach(game -> game.getStores().add(store));
    }

    @Transactional
    public void updateStoreUsers(Stores store, Set<Long> userIds) {
        Set<Users> newUsers = userIds.stream()
                .map(userId -> usersDao.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId)))
                .collect(Collectors.toSet());

        store.getStoreUsers().clear();
        store.getStoreUsers().addAll(newUsers);
        newUsers.forEach(user -> user.getUserStores().add(store));
    }

    @Transactional
    public void deleteStore(Long storeId) throws ResourceNotFoundException {
        Stores store = storesDao.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));

        store.getStoreGames().forEach(game -> game.getStores().remove(store));
        store.getStoreUsers().forEach(user -> user.getUserStores().remove(store));

        storesDao.delete(store);
    }

    public StoresData getStoreById(Long storeId) {
        Stores store = storesDao.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));

        return convertToStoresData(store);
    }

    public List<StoresData> searchStoresByName(String query) {
        List<Stores> stores = storesDao.findByStoreNameContainingIgnoreCase(query);
        return stores.stream()
                .map(this::convertToStoresData)
                .collect(Collectors.toList());
    }
    // TODO: Data produces to the CLI incorrectly from menu.
    @Transactional
    public List<StoresData> getAllStores() {
        List<Stores> stores = storesDao.findAll();
        return stores.stream()
                .map(this::convertToStoresData)
                .collect(Collectors.toList());
    }

    private StoresData convertToStoresData(Stores store) {
        StoresData storesData = new StoresData();
        storesData.setStoreId(store.getStoreId());
        storesData.setStoreName(store.getStoreName());
        storesData.setStoreDisabilityCheck(store.getStoreDisabilityCheck());
        storesData.setStoreCity(store.getStoreCity());
        storesData.setStoreRegion(store.getStoreRegion());
        storesData.setOutsideFood(store.getOutsideFood());
        storesData.setStoreAddress(store.getStoreAddress());
        storesData.setStorePhone(store.getStorePhone());
        storesData.setStoreEmail(store.getStoreEmail());
        storesData.setStoreParking(store.getStoreParking());
        storesData.setStoreGameIds(store.getStoreGames().stream().map(Games::getGameId).collect(Collectors.toSet()));
        storesData.setStoreUserIds(store.getStoreUsers().stream().map(Users::getUserId).collect(Collectors.toSet()));
        return storesData;
    }

    @Transactional
    public void deleteStores(List<Long> storeIds) {
        List<Stores> stores = storesDao.findAllById(storeIds);
        stores.forEach(store -> {
            store.getStoreGames().forEach(game -> game.getStores().remove(store));
            store.getStoreUsers().forEach(user -> user.getUserStores().remove(store));
            storesDao.delete(store);
        });
    }
}
