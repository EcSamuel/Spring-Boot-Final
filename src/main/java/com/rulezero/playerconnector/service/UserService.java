package com.rulezero.playerconnector.service;

import com.rulezero.playerconnector.controller.model.AvailabilityData;
import com.rulezero.playerconnector.controller.model.UsersData;
import com.rulezero.playerconnector.dao.GamesDao;
import com.rulezero.playerconnector.dao.StoresDao;
import com.rulezero.playerconnector.dao.UsersDao;
import com.rulezero.playerconnector.entity.Availability;
import com.rulezero.playerconnector.entity.Games;
import com.rulezero.playerconnector.entity.Stores;
import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.dao.AvailabilityDao;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private AvailabilityDao availabilityDao;

    @Autowired
    private GamesDao gamesDao;

    @Autowired
    private StoresDao storesDao;

    @Autowired
    private AvailabilityService availabilityService;

    @Transactional
    public UsersData saveUser(UsersData usersData) {
        Users user = convertToEntity(usersData);
        Users savedUser = usersDao.save(user);
        return convertToUsersData(savedUser);
    }

    private Users convertToEntity(UsersData usersData) {
        Users user = new Users();
        user.setFirstName(usersData.getFirstName());
        user.setLastName(usersData.getLastName());
        user.setUserPhone(usersData.getUserPhone());
        user.setUserAddress(usersData.getUserAddress());
        user.setUserCity(usersData.getUserCity());
        user.setUserRegion(usersData.getUserRegion());
        user.setUserLoginName(usersData.getUserLoginName());
        user.setUserEmail(usersData.getUserEmail());
        user.setPassword(usersData.getPassword());

        if (usersData.getAvailabilityId() != null) {
            Availability availability = new Availability();
            availability.setAvailabilityId(usersData.getAvailabilityId());
            availability.setStartTime(usersData.getStartTime());
            availability.setEndTime(usersData.getEndTime());
            availability.setDayOfWeek(usersData.getDayOfWeek());
            user.setUserAvailability(availability);
        }

        if (usersData.getGameIds() != null) {
            Set<Games> games = usersData.getGameIds().stream()
                    .map(gameId -> gamesDao.findById(gameId)
                            .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId)))
                    .collect(Collectors.toSet());
            user.setUserGames(games);
        }

        return user;
    }

//    @Transactional
//    public UsersData patchUser(Long userId, UsersData usersData) throws ResourceNotFoundException {
//        Users existingUser = usersDao.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
//
//        updateUserFields(existingUser, usersData);
//
//        Users updatedUser = usersDao.save(existingUser);
//        return convertToUsersData(updatedUser);
//    }

    private void updateUserFields(Users user, UsersData usersData) {
        if (usersData.getFirstName() != null) {
            user.setFirstName(usersData.getFirstName());
        }
        if (usersData.getLastName() != null) {
            user.setLastName(usersData.getLastName());
        }
        if (usersData.getUserPhone() != null) {
            user.setUserPhone(usersData.getUserPhone());
        }
        if (usersData.getUserAddress() != null) {
            user.setUserAddress(usersData.getUserAddress());
        }
        if (usersData.getUserCity() != null) {
            user.setUserCity(usersData.getUserCity());
        }
        if (usersData.getUserRegion() != null) {
            user.setUserRegion(usersData.getUserRegion());
        }
        if (usersData.getUserLoginName() != null) {
            user.setUserLoginName(usersData.getUserLoginName());
        }
        if (usersData.getUserEmail() != null) {
            user.setUserEmail(usersData.getUserEmail());
        }
        if (usersData.getStartTime() != null || usersData.getEndTime() != null || usersData.getDayOfWeek() != null) {
            Availability availability = user.getUserAvailability();
            if (availability == null) {
                availability = new Availability();
                availability.setUser(user);
            }
            if (usersData.getStartTime() != null) {
                availability.setStartTime(usersData.getStartTime());
            }
            if (usersData.getEndTime() != null) {
                availability.setEndTime(usersData.getEndTime());
            }
            if (usersData.getDayOfWeek() != null) {
                availability.setDayOfWeek(usersData.getDayOfWeek());
            }
            user.setUserAvailability(availability);
        }
        if (usersData.getGameIds() != null) {
            updateUserGames(user, usersData.getGameIds());
        }
    }

    @Transactional
    public void updateUserStores(Long userId, Set<Long> storeIds) throws ResourceNotFoundException {
        Users user = usersDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Set<Stores> newStores = storeIds.stream()
                .map(storeId -> storesDao.findById(storeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId)))
                .collect(Collectors.toSet());

        // Remove stores not in the new set
        user.getUserStores().removeIf(store -> !newStores.contains(store));

        // Add new stores
        newStores.forEach(store -> {
            if (!user.getUserStores().contains(store)) {
                user.getUserStores().add(store);
                store.getStoreUsers().add(user);
            }
        });

        usersDao.save(user);
    }

    public Users getUserEntityById(Long userId) {
        return usersDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Transactional
    public void updateUserGames(Users user, Set<Long> gameIds) {
        Set<Games> newGames = gameIds.stream()
                .map(gameId -> gamesDao.findById(gameId)
                        .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId)))
                .collect(Collectors.toSet());

        // Remove games not in the new set
        user.getUserGames().removeIf(game -> !newGames.contains(game));

        // Add new games
        newGames.forEach(game -> {
            if (!user.getUserGames().contains(game)) {
                user.getUserGames().add(game);
                game.getPlayers().add(user);
            }
        });
    }

    @Transactional
    public void deleteUser(Long userId) throws ResourceNotFoundException {
        Users user = usersDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Remove the user from all games and stores
        user.getUserGames().forEach(game -> game.getPlayers().remove(user));
        user.getUserStores().forEach(store -> store.getStoreUsers().remove(user));

        usersDao.delete(user);
    }

//    public UsersData getUserById(Long userId) {
//        Users user = usersDao.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
//
//        return convertToUsersData(user);
//    }

    public List<UsersData> searchUsersByName(String query) {
        List<Users> users = usersDao.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
        return users.stream()
                .map(this::convertToUsersData)
                .collect(Collectors.toList());
    }
//    @Transactional
//    public List<UsersData> getAllUsers() {
//        List<Users> users = usersDao.findAll();
//        return users.stream()
//                .map(this::convertToUsersData)
//                .collect(Collectors.toList());
//    }

    private UsersData convertToUsersData(Users user) {
        UsersData usersData = new UsersData();
        usersData.setUserId(user.getUserId());
        usersData.setFirstName(user.getFirstName());
        usersData.setLastName(user.getLastName());
        usersData.setUserPhone(user.getUserPhone());
        usersData.setUserAddress(user.getUserAddress());
        usersData.setUserCity(user.getUserCity());
        usersData.setUserRegion(user.getUserRegion());
        usersData.setUserLoginName(user.getUserLoginName());
        usersData.setUserEmail(user.getUserEmail());
        Availability availability = user.getUserAvailability();
        if (availability != null) {
            usersData.setAvailabilityId(availability.getAvailabilityId());
            usersData.setStartTime(availability.getStartTime());
            usersData.setEndTime(availability.getEndTime());
            usersData.setDayOfWeek(availability.getDayOfWeek());
        }
        return usersData;
    }

    @Transactional
    public void deleteUsers(List<Long> userIds) {
        List<Users> users = usersDao.findAllById(userIds);
        users.forEach(user -> {
            user.getUserGames().forEach(game -> game.getPlayers().remove(user));
            user.getUserStores().forEach(store -> store.getStoreUsers().remove(user));
            usersDao.delete(user);
        });
    }

    @Transactional
    public UsersData updateUserAvailability(Long userId, Long availabilityId) throws ResourceNotFoundException {
        Users user = usersDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        AvailabilityData availabilityData = availabilityService.getAvailabilityById(availabilityId);

        Availability availability = user.getUserAvailability();
        if (availability == null) {
            availability = new Availability();
            availability.setUser(user);
        }

        availability.setStartTime(availabilityData.getStartTime());
        availability.setEndTime(availabilityData.getEndTime());
        availability.setDayOfWeek(availabilityData.getDayOfWeek());

        user.setUserAvailability(availability);
        Users updatedUser = usersDao.save(user);

        return convertToUsersData(updatedUser);
    }
    // originally these next two methods had readyOnly = true in them but I can't get that to exist
    @Transactional()
    public UsersData getUserById(Long userId) {
        Users user = usersDao.findByIdWithAssociations(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return convertToUsersData(user);
    }

    @Transactional()
    public List<UsersData> getAllUsers() {
        List<Users> users = usersDao.findAllWithAssociations();
        return users.stream()
                .map(this::convertToUsersData)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsersData patchUser(Long userId, UsersData usersData) throws ResourceNotFoundException {
        Users existingUser = usersDao.findByIdWithAssociations(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        updateUserFields(existingUser, usersData);

        Users updatedUser = usersDao.save(existingUser);
        return convertToUsersData(updatedUser);
    }
}