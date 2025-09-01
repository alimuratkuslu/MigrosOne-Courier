package com.migrosone.courier_tracking.application.usecase;

import com.migrosone.courier_tracking.domain.model.Courier;
import com.migrosone.courier_tracking.domain.model.CourierLocation;
import com.migrosone.courier_tracking.domain.model.Distance;
import com.migrosone.courier_tracking.domain.port.CourierLocationRepository;
import com.migrosone.courier_tracking.domain.service.DistanceCalculationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTotalTravelDistanceUseCaseTest {

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @Mock
    private DistanceCalculationStrategy distanceCalculationStrategy;

    @InjectMocks
    private GetTotalTravelDistanceUseCase getTotalTravelDistanceUseCase;

    private static final String COURIER_ID = "courier-1";
    private Courier courier;
    private LocalDateTime baseTime;

    @BeforeEach
    void setUp() {
        courier = new Courier(COURIER_ID);
        baseTime = LocalDateTime.now();
    }

    @Test
    void getTotalTravelDistance_WithMultipleLocations_ShouldCalculateCorrectDistance() {
        CourierLocation location1 = new CourierLocation(courier, 40.9923307, 29.1244229, baseTime);
        CourierLocation location2 = new CourierLocation(courier, 40.9923308, 29.1244230, baseTime.plusMinutes(5));
        CourierLocation location3 = new CourierLocation(courier, 40.9923309, 29.1244231, baseTime.plusMinutes(10));
        
        List<CourierLocation> locations = Arrays.asList(location1, location2, location3);
        
        when(courierLocationRepository.findByCourierId(COURIER_ID)).thenReturn(locations);
        when(distanceCalculationStrategy.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(new Distance(100.0))
            .thenReturn(new Distance(150.0));

        double totalDistance = getTotalTravelDistanceUseCase.getTotalTravelDistance(COURIER_ID);

        assertEquals(250.0, totalDistance, 0.001);
    }

    @Test
    void getTotalTravelDistance_WithSingleLocation_ShouldReturnZero() {
        CourierLocation singleLocation = new CourierLocation(courier, 40.9923307, 29.1244229, baseTime);
        List<CourierLocation> locations = Collections.singletonList(singleLocation);
        
        when(courierLocationRepository.findByCourierId(COURIER_ID)).thenReturn(locations);

        double totalDistance = getTotalTravelDistanceUseCase.getTotalTravelDistance(COURIER_ID);

        assertEquals(0.0, totalDistance, 0.001);
    }
}