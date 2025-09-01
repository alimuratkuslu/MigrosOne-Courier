package com.migrosone.courier_tracking.infrastructure.adapter;

import com.migrosone.courier_tracking.domain.port.CourierLocationRepository;
import com.migrosone.courier_tracking.domain.model.CourierLocation;
import com.migrosone.courier_tracking.infrastructure.repository.JpaCourierLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourierLocationRepositoryAdapter implements CourierLocationRepository {
    
    private final JpaCourierLocationRepository jpaCourierLocationRepository;
    
    @Override
    public void save(CourierLocation location) {
        jpaCourierLocationRepository.save(location);
    }
    
    @Override
    public List<CourierLocation> findByCourierId(String courierId) {
        return jpaCourierLocationRepository.findByCourier_IdOrderByTimestamp(courierId);
    }
    
    @Override
    public CourierLocation findLastLocationByCourierId(String courierId) {
        return jpaCourierLocationRepository.findLastLocationByCourierId(courierId).orElse(null);
    }
}