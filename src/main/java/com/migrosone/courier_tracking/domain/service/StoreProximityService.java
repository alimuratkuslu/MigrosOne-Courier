package com.migrosone.courier_tracking.domain.service;

import com.migrosone.courier_tracking.domain.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreProximityService {
    
    private final DistanceCalculationStrategy distanceCalculationStrategy;
    private final List<StoreEntranceObserver> observers = new ArrayList<>();
    private static final double STORE_RADIUS_METERS = 100.0;

    public StoreProximityService(DistanceCalculationStrategy distanceCalculationStrategy) {
        this.distanceCalculationStrategy = distanceCalculationStrategy;
    }

    public void addObserver(StoreEntranceObserver observer) {
        observers.add(observer);
    }
    
    public boolean isWithinStoreRadius(CourierLocation location, Store store) {
        Distance distance = distanceCalculationStrategy.calculateDistance(
            location.getLatitude(), 
            location.getLongitude(),
            store.getLatitude(),
            store.getLongitude()
        );
        return distance.isWithinRadius(STORE_RADIUS_METERS);
    }

    public void notifyStoreEntrance(Courier courier, String storeName, LocalDateTime entranceTime) {
        StoreEntrance entrance = new StoreEntrance(courier, storeName, entranceTime);
        
        for (StoreEntranceObserver observer : observers) {
            observer.onStoreEntrance(entrance);
        }
    }
}