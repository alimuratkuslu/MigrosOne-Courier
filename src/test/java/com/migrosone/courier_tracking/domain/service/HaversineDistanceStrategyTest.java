package com.migrosone.courier_tracking.domain.service;

import com.migrosone.courier_tracking.domain.model.Distance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaversineDistanceStrategyTest {

    private HaversineDistanceStrategy haversineDistanceStrategy;

    @BeforeEach
    void setUp() {
        haversineDistanceStrategy = new HaversineDistanceStrategy();
    }

    @Test
    void calculateDistance_SameCoordinates_ShouldReturnZero() {
        double lat1 = 40.9923307;
        double lon1 = 29.1244229;

        Distance distance = haversineDistanceStrategy.calculateDistance(lat1, lon1, lat1, lon1);

        assertEquals(0.0, distance.getMeters(), 0.001);
    }

    @Test
    void calculateDistance_KnownDistance_ShouldReturnCorrectValue() {
        double atasehirLat = 40.9923307;
        double atasehirLon = 29.1244229;
        double novadaLat = 41.0082376;
        double novadaLon = 28.7784296;

        Distance distance = haversineDistanceStrategy.calculateDistance(
            atasehirLat, atasehirLon, novadaLat, novadaLon
        );

        assertTrue(distance.getMeters() > 20000);
        assertTrue(distance.getMeters() < 30000);
    }

    @Test
    void calculateDistance_ShortDistance_ShouldReturnCorrectValue() {
        double lat1 = 40.9923307;
        double lon1 = 29.1244229;
        double lat2 = 40.9933307;
        double lon2 = 29.1244229;

        Distance distance = haversineDistanceStrategy.calculateDistance(lat1, lon1, lat2, lon2);

        assertTrue(distance.getMeters() > 90);
        assertTrue(distance.getMeters() < 130);
    }
}