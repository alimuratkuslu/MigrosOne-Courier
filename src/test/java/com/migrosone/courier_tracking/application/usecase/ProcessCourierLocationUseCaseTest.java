package com.migrosone.courier_tracking.application.usecase;

import com.migrosone.courier_tracking.domain.exception.InvalidCoordinateException;
import com.migrosone.courier_tracking.domain.model.Courier;
import com.migrosone.courier_tracking.domain.model.CourierLocation;
import com.migrosone.courier_tracking.domain.model.Store;
import com.migrosone.courier_tracking.domain.model.StoreEntrance;
import com.migrosone.courier_tracking.domain.port.CourierLocationRepository;
import com.migrosone.courier_tracking.domain.port.CourierRepository;
import com.migrosone.courier_tracking.domain.port.StoreRepository;
import com.migrosone.courier_tracking.domain.port.StoreEntranceRepository;
import com.migrosone.courier_tracking.domain.service.StoreProximityService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessCourierLocationUseCaseTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierLocationRepository courierLocationRepository;
    
    @Mock
    private StoreRepository storeRepository;
    
    @Mock
    private StoreEntranceRepository storeEntranceRepository;
    
    @Mock
    private StoreProximityService storeProximityService;

    @InjectMocks
    private ProcessCourierLocationUseCase processLocationUseCase;

    private static final String COURIER_ID = "courier-1";
    private static final double LATITUDE = 40.9923307;
    private static final double LONGITUDE = 29.1244229;
    private LocalDateTime timestamp;
    private Courier testCourier;
    private Store testStore;

    @BeforeEach
    void setUp() {
        timestamp = LocalDateTime.now();
        lenient().when(storeRepository.findAll()).thenReturn(Collections.emptyList());
        testCourier = new Courier(COURIER_ID);
        testStore = Store.builder()
                .id(1L)
                .name("Test Migros Store")
                .latitude(40.9923307)
                .longitude(29.1244229)
                .build();
    }

    @Test
    void processLocation_NewCourier_ShouldCreateCourierAndSaveLocation() {
        when(courierRepository.existsById(COURIER_ID)).thenReturn(false);

        processLocationUseCase.processLocation(COURIER_ID, LATITUDE, LONGITUDE, timestamp);

        verify(courierRepository).existsById(COURIER_ID);
        verify(courierRepository).save(any(Courier.class));
        verify(courierLocationRepository).save(any(CourierLocation.class));

        verify(courierRepository).save(argThat(courier -> 
            COURIER_ID.equals(courier.getId()) && 
            courier.isActive() &&
            courier.getCreatedAt() != null
        ));

        verify(courierLocationRepository).save(argThat(location ->
            LATITUDE == location.getLatitude() &&
            LONGITUDE == location.getLongitude() &&
            timestamp.equals(location.getTimestamp())
        ));
    }

    @Test
    void processLocation_ExistingCourier_ShouldOnlySaveLocation() {
        Courier existingCourier = new Courier(COURIER_ID);
        when(courierRepository.existsById(COURIER_ID)).thenReturn(true);
        when(courierRepository.findById(COURIER_ID)).thenReturn(Optional.of(existingCourier));

        processLocationUseCase.processLocation(COURIER_ID, LATITUDE, LONGITUDE, timestamp);

        verify(courierRepository).existsById(COURIER_ID);
        verify(courierRepository).findById(COURIER_ID);
        verify(courierRepository, never()).save(any(Courier.class));
        verify(courierLocationRepository).save(any(CourierLocation.class));

        verify(courierLocationRepository).save(argThat(location ->
            location.getCourier().equals(existingCourier) &&
            LATITUDE == location.getLatitude() &&
            LONGITUDE == location.getLongitude() &&
            timestamp.equals(location.getTimestamp())
        ));
    }

    @Test
    void processLocation_WithStoresNearby_ShouldCheckProximity() {
        when(courierRepository.existsById(COURIER_ID)).thenReturn(true);
        when(courierRepository.findById(COURIER_ID)).thenReturn(Optional.of(testCourier));

        List<Store> stores = Arrays.asList(testStore);
        when(storeRepository.findAll()).thenReturn(stores);

        // Not in store radius
        when(storeProximityService.isWithinStoreRadius(any(CourierLocation.class), eq(testStore)))
                .thenReturn(false);

        processLocationUseCase.processLocation(COURIER_ID, LATITUDE, LONGITUDE, timestamp);

        verify(storeRepository).findAll();
        verify(storeProximityService).isWithinStoreRadius(any(CourierLocation.class), eq(testStore));
        verify(storeEntranceRepository, never()).findLastEntranceByCourierIdAndStoreName(any(), any());
    }

    @Test
    void processLocation_WithinStoreRadius_ShouldHandleStoreEntrance() {
        when(courierRepository.existsById(COURIER_ID)).thenReturn(true);
        when(courierRepository.findById(COURIER_ID)).thenReturn(Optional.of(testCourier));

        List<Store> stores = Arrays.asList(testStore);
        when(storeRepository.findAll()).thenReturn(stores);

        // In store radius
        when(storeProximityService.isWithinStoreRadius(any(CourierLocation.class), eq(testStore)))
                .thenReturn(true);

        when(storeEntranceRepository.findLastEntranceByCourierIdAndStoreName(COURIER_ID, testStore.getName()))
                .thenReturn(Optional.empty());

        processLocationUseCase.processLocation(COURIER_ID, LATITUDE, LONGITUDE, timestamp);

        verify(storeRepository).findAll();
        verify(storeProximityService).isWithinStoreRadius(any(CourierLocation.class), eq(testStore));
        verify(storeEntranceRepository).findLastEntranceByCourierIdAndStoreName(COURIER_ID, testStore.getName());
        verify(storeEntranceRepository).save(any(StoreEntrance.class));
        verify(storeProximityService).notifyStoreEntrance(testCourier, testStore.getName(), timestamp);
    }

    @Test
    void processLocation_WithinStoreRadius_RecentEntrance_ShouldNotCreateDuplicate() {
        when(courierRepository.existsById(COURIER_ID)).thenReturn(true);
        when(courierRepository.findById(COURIER_ID)).thenReturn(Optional.of(testCourier));

        List<Store> stores = Arrays.asList(testStore);
        when(storeRepository.findAll()).thenReturn(stores);

        when(storeProximityService.isWithinStoreRadius(any(CourierLocation.class), eq(testStore)))
                .thenReturn(true);

        // Less than 1 minute entry
        StoreEntrance recentEntrance = new StoreEntrance(testCourier, testStore.getName(), timestamp.minusSeconds(30));
        when(storeEntranceRepository.findLastEntranceByCourierIdAndStoreName(COURIER_ID, testStore.getName()))
                .thenReturn(Optional.of(recentEntrance));

        processLocationUseCase.processLocation(COURIER_ID, LATITUDE, LONGITUDE, timestamp);

        verify(storeRepository).findAll();
        verify(storeProximityService).isWithinStoreRadius(any(CourierLocation.class), eq(testStore));
        verify(storeEntranceRepository).findLastEntranceByCourierIdAndStoreName(COURIER_ID, testStore.getName());
        verify(storeEntranceRepository, never()).save(any(StoreEntrance.class));
        verify(storeProximityService, never()).notifyStoreEntrance(any(), any(), any());
    }

    @Test
    void processLocation_InvalidCoordinates_ShouldThrowInvalidCoordinateException() {
        double invalidLatitude = 91.0;
        double invalidLongitude = 181.0;

        InvalidCoordinateException exception = assertThrows(InvalidCoordinateException.class, () -> 
            processLocationUseCase.processLocation(COURIER_ID, invalidLatitude, invalidLongitude, timestamp)
        );
        
        assertTrue(exception.getMessage().contains("Invalid coordinates provided"));
        assertTrue(exception.getMessage().contains("latitude=91.000000"));
        assertTrue(exception.getMessage().contains("longitude=181.000000"));

        verify(courierRepository, never()).existsById(any());
        verify(courierRepository, never()).save(any(Courier.class));
        verify(courierLocationRepository, never()).save(any(CourierLocation.class));
    }
}