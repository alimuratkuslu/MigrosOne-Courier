package com.migrosone.courier_tracking.domain.service;

import com.migrosone.courier_tracking.domain.model.Distance;
import org.springframework.stereotype.Component;

@Component
public class HaversineDistanceStrategy implements DistanceCalculationStrategy {
    
    private static final double EARTH_RADIUS_METERS = 6371000;
    
    @Override
    public Distance calculateDistance(double fromLat, double fromLng, double toLat, double toLng) {
        double lat1Rad = Math.toRadians(fromLat);
        double lat2Rad = Math.toRadians(toLat);
        double deltaLatRad = Math.toRadians(toLat - fromLat);
        double deltaLngRad = Math.toRadians(toLng - fromLng);
        
        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLngRad / 2) * Math.sin(deltaLngRad / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceInMeters = EARTH_RADIUS_METERS * c;
        
        return new Distance(distanceInMeters);
    }
}