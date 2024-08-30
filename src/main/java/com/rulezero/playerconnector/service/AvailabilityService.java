package com.rulezero.playerconnector.service;

import com.rulezero.playerconnector.controller.model.AvailabilityData;
import com.rulezero.playerconnector.dao.UsersDao;
import com.rulezero.playerconnector.entity.Availability;
import com.rulezero.playerconnector.entity.Users;
import com.rulezero.playerconnector.exception.ResourceNotFoundException;
import com.rulezero.playerconnector.dao.AvailabilityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityDao availabilityDao;

    @Autowired
    private UsersDao usersDao;

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
}
