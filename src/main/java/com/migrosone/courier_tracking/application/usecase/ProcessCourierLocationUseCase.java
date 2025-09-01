package com.migrosone.courier_tracking.application.usecase;

import com.migrosone.courier_tracking.domain.model.*;
import com.migrosone.courier_tracking.domain.port.*;
import com.migrosone.courier_tracking.domain.service.StoreProximityService;
import com.migrosone.courier_tracking.domain.exception.InvalidCoordinateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcessCourierLocationUseCase {
    
    private final CourierLocationRepository courierLocationRepository;
    private final CourierRepository courierRepository;
    private final StoreRepository storeRepository;
    private final StoreEntranceRepository storeEntranceRepository;
    private final StoreProximityService storeProximityService;
    
    public void processLocation(String courierId, double latitude, double longitude, LocalDateTime timestamp) {
        if (!isValidCoordinate(latitude, longitude)) {
            throw new InvalidCoordinateException(latitude, longitude);
        }

        Courier courier;
        if (!courierRepository.existsById(courierId)) {
            courier = new Courier(courierId);
            courierRepository.save(courier);
        } else {
            courier = courierRepository.findById(courierId).orElseThrow();
        }
        
        CourierLocation location = new CourierLocation(courier, latitude, longitude, timestamp);
        
        courierLocationRepository.save(location);
        
        checkStoreProximity(location);
    }
    
    private void checkStoreProximity(CourierLocation location) {
        List<Store> stores = storeRepository.findAll();
        
        for (Store store : stores) {
            if (storeProximityService.isWithinStoreRadius(location, store)) {
                handleStoreEntrance(location, store);
            }
        }
    }
    
    private void handleStoreEntrance(CourierLocation location, Store store) {
        Optional<StoreEntrance> lastEntrance = storeEntranceRepository
            .findLastEntranceByCourierIdAndStoreName(location.getCourierId(), store.getName());
        
        boolean shouldLogEntrance = lastEntrance
            .map(entrance -> !entrance.isWithinOneMinuteOf(location.getTimestamp()))
            .orElse(true);
        
        if (shouldLogEntrance) {
            StoreEntrance entrance = new StoreEntrance(
                location.getCourier(), 
                store.getName(), 
                location.getTimestamp()
            );
            storeEntranceRepository.save(entrance);
            // Kafka Push Event
            storeProximityService.notifyStoreEntrance(
                location.getCourier(), 
                store.getName(), 
                location.getTimestamp()
            );
        }
    }
    
    private boolean isValidCoordinate(double latitude, double longitude) {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }
}