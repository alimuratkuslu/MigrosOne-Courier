package com.migrosone.courier_tracking.infrastructure.adapter;

import com.migrosone.courier_tracking.domain.model.Courier;
import com.migrosone.courier_tracking.infrastructure.repository.JpaCourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierRepositoryAdapterTest {

    @Mock
    private JpaCourierRepository jpaCourierRepository;

    @InjectMocks
    private CourierRepositoryAdapter courierRepositoryAdapter;

    private static final String COURIER_ID = "courier-1";
    private Courier testCourier;

    @BeforeEach
    void setUp() {
        testCourier = Courier.builder()
                .id(COURIER_ID)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
    }

    @Test
    void save_ShouldSaveCourierToJpaRepository() {
        courierRepositoryAdapter.save(testCourier);

        verify(jpaCourierRepository).save(testCourier);
    }

    @Test
    void findById_ExistingCourier_ShouldReturnCourier() {
        when(jpaCourierRepository.findById(COURIER_ID)).thenReturn(Optional.of(testCourier));

        Optional<Courier> result = courierRepositoryAdapter.findById(COURIER_ID);

        assertTrue(result.isPresent());
        assertEquals(testCourier, result.get());
        verify(jpaCourierRepository).findById(COURIER_ID);
    }

    @Test
    void findById_NonExistingCourier_ShouldReturnEmpty() {
        when(jpaCourierRepository.findById(COURIER_ID)).thenReturn(Optional.empty());

        Optional<Courier> result = courierRepositoryAdapter.findById(COURIER_ID);

        assertFalse(result.isPresent());
        verify(jpaCourierRepository).findById(COURIER_ID);
    }

    @Test
    void existsById_ExistingCourier_ShouldReturnTrue() {
        when(jpaCourierRepository.existsById(COURIER_ID)).thenReturn(true);

        boolean exists = courierRepositoryAdapter.existsById(COURIER_ID);

        assertTrue(exists);
        verify(jpaCourierRepository).existsById(COURIER_ID);
    }
}