package com.migrosone.courier_tracking.infrastructure.observer;

import com.migrosone.courier_tracking.domain.model.StoreEntrance;
import com.migrosone.courier_tracking.domain.service.StoreEntranceObserver;
import com.migrosone.courier_tracking.domain.service.StoreProximityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Slf4j
public class LoggingStoreEntranceObserver implements StoreEntranceObserver {
    
    private final StoreProximityService storeProximityService;

    public LoggingStoreEntranceObserver(StoreProximityService storeProximityService) {
        this.storeProximityService = storeProximityService;
    }

    @PostConstruct
    public void registerAsObserver() {
        storeProximityService.addObserver(this);
        log.info("LoggingStoreEntranceObserver registered successfully");
    }
    
    @Override
    public void onStoreEntrance(StoreEntrance entrance) {
        log.info("--- STORE ENTRANCE DETECTED ---");
        log.info("Courier ID: {}", entrance.getCourierId());
        log.info("Store Name: {}", entrance.getStoreName());
        log.info("Entrance Time: {}", entrance.getEntranceTime());
        log.info("--------------------------------");
    }
}