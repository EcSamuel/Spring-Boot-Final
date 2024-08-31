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
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityDao availabilityDao;

    @Autowired
    private UsersDao usersDao;

    @Transactional
    public AvailabilityData saveAvailability(AvailabilityData availabilityData) {
        Users user = usersDao.findById(availabilityData.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Availability availability = new Availability();
        availability.setDayOfWeek(availabilityData.getDayOfWeek());
        availability.setStartTime(availabilityData.getStartTime());
        availability.setEndTime(availabilityData.getEndTime());
        availability.setUser(user);

        Availability savedAvailability = availabilityDao.save(availability);

        return mapToData(savedAvailability);
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

    private AvailabilityData mapToData(Availability availability) {
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
    public AvailabilityData updateAvailability(Long availabilityId, AvailabilityData availabilityData) throws ResourceNotFoundException {
        Availability existingAvailability = availabilityDao.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with id: " + availabilityId));

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

    public void deleteAvailability(Long availabilityId) {
    }

    // unsure if I'll ever need this one
//    public List<AvailabilityData> getAvailabilityByDateRange(LocalDate startDate, LocalDate endDate) {
//        List<Availability> availabilities = availabilityDao.findByDateRange(startDate, endDate);
//        return availabilities.stream()
//                .map(this::mapToData)
//                .collect(Collectors.toList());
//    }


}
