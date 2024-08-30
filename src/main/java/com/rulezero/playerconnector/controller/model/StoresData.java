package com.rulezero.playerconnector.controller.model;

import lombok.Data;

import java.util.Set;

@Data
public class StoresData {
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeCity;
    private String storePhone;
    private String storeEmail;
    private Boolean storeDisabilityCheck;
    private Boolean outsideFood;
    private Long hoursId;
    private String storeRegion;
    private Integer storeParking;
    //    private StoreHoursData storeHours;
    //    private Set<Long> storeProducts;
    private Set<Long> storeGameIds;
    private Set<Long> storeUserIds;
}
