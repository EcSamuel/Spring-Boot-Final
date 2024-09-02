package com.rulezero.playerconnector.service;

import com.rulezero.playerconnector.controller.model.AvailabilityData;
import com.rulezero.playerconnector.dao.UsersDao;
import com.rulezero.playerconnector.entity.Availability;
import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import com.rulezero.playerconnector.dao.AvailabilityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityDao availabilityDao;

    @Autowired
    private UsersDao usersDao;

    @Transactional
    public Availability saveAvailability(Availability availability, Users users) {
        System.out.println("Availabilty entity before saving:" + availability);
        System.out.println("User entity associated" + users);
        if (users == null || availability == null) {
            throw new IllegalArgumentException("User and Availability must not be null");
        }

        if (availability.getAvailabilityId() != null) {
            Optional<Availability> existingAvailability = availabilityDao.findById(availability.getAvailabilityId());

            if (existingAvailability.isPresent()) {
                Availability availToUpdate = existingAvailability.get();
                availToUpdate.setStartTime(availability.getStartTime());
                availToUpdate.setEndTime(availability.getEndTime());
                availToUpdate.setDayOfWeek(availability.getDayOfWeek());
                return availabilityDao.save(availToUpdate);
            }
        }
        // If the availability ID is null or it doesn't exist in the database
        availability.setUser(users);
        // This sout might be written incorrectly.
        System.out.println("Saved Availability entity:" + availability);
        return availabilityDao.save(availability);
    }


    public AvailabilityData getAvailabilityById(Long availabilityId) {
        Availability availability = availabilityDao.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with id: " + availabilityId));
        return mapToData(availability);
    }

    public List<AvailabilityData> getAllAvailabilities() {
        List<Availability> availabilities = availabilityDao.findAll();
        return availabilities.stream()
                .map(this::mapToData)
                .collect(Collectors.toList());
    }

    public List<AvailabilityData> getAvailabilityByUserId(Long userId) {
        List<Availability> availabilities = availabilityDao.findByUser_UserId(userId);
        return availabilities.stream().map(this::mapToData).collect(Collectors.toList());
    }

    public AvailabilityData mapToData(Availability availability) {
        AvailabilityData data = new AvailabilityData();
        data.setAvailabilityId(availability.getAvailabilityId());
        data.setDayOfWeek(availability.getDayOfWeek());
        data.setStartTime(availability.getStartTime());
        data.setEndTime(availability.getEndTime());
        data.setUserId(availability.getUser().getUserId());
        return data;
    }

    public List<AvailabilityData> getAllAvailability() {
        List<Availability> availabilities = availabilityDao.findAll();
        return availabilities.stream()
                .map(this::mapToData)
                .collect(Collectors.toList());
    }

    @Transactional
    public AvailabilityData updateAvailability(AvailabilityData availabilityData) {
        Availability existingAvailability = availabilityDao.findById(availabilityData.getAvailabilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with id: " + availabilityData.getAvailabilityId()));

        existingAvailability.setDayOfWeek(availabilityData.getDayOfWeek());
        existingAvailability.setStartTime(availabilityData.getStartTime());
        existingAvailability.setEndTime(availabilityData.getEndTime());

        Availability updatedAvailability = availabilityDao.save(existingAvailability);
        return mapToData(updatedAvailability);
    }

    @Transactional
    public void deleteAvailabilityByUserId(Long userId) {
        List<Availability> availabilities = availabilityDao.findByUser_UserId(userId);
        availabilityDao.deleteAll(availabilities);
    }

    @Transactional
    public void deleteAvailabilityById(Long availabilityId) {
        Availability availability = availabilityDao.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with id: " + availabilityId));
        availabilityDao.delete(availability);
    }

    public Availability convertToEntity(AvailabilityData availabilityData) {
        Availability availability = new Availability();
        availability.setAvailabilityId(availabilityData.getAvailabilityId());
        availability.setDayOfWeek(availabilityData.getDayOfWeek());
        availability.setStartTime(availabilityData.getStartTime());
        availability.setEndTime(availabilityData.getEndTime());
        if (availabilityData.getUserId() != null) {
            Users user = usersDao.findById(availabilityData.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + availabilityData.getUserId()));
        }
        return availability;
    }

    // unsure if I'll ever need this one
//    public List<AvailabilityData> getAvailabilityByDateRange(LocalDate startDate, LocalDate endDate) {
//        List<Availability> availabilities = availabilityDao.findByDateRange(startDate, endDate);
//        return availabilities.stream()
//                .map(this::mapToData)
//                .collect(Collectors.toList());
//    }


}
