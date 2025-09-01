package com.migrosone.courier_tracking.application.usecase;

import com.migrosone.courier_tracking.domain.model.CourierLocation;
import com.migrosone.courier_tracking.domain.model.Distance;
import com.migrosone.courier_tracking.domain.port.CourierLocationRepository;
import com.migrosone.courier_tracking.domain.service.DistanceCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTotalTravelDistanceUseCase {
    
    private final CourierLocationRepository courierLocationRepository;
    private final DistanceCalculationStrategy distanceCalculationStrategy;
    
    public Double getTotalTravelDistance(String courierId) {
        List<CourierLocation> locations = courierLocationRepository.findByCourierId(courierId);
        
        if (locations.size() < 2) {
            return 0.0;
        }
        
        Distance totalDistance = new Distance(0.0);
        
        for (int i = 1; i < locations.size(); i++) {
            CourierLocation previous = locations.get(i - 1);
            CourierLocation current = locations.get(i);
            
            Distance segmentDistance = distanceCalculationStrategy.calculateDistance(
                previous.getLatitude(), 
                previous.getLongitude(),
                current.getLatitude(),
                current.getLongitude()
            );
            
            totalDistance = totalDistance.add(segmentDistance);
        }
        
        return totalDistance.getMeters();
    }
}