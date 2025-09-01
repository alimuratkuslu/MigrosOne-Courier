package com.migrosone.courier_tracking.infrastructure.adapter;

import com.migrosone.courier_tracking.domain.model.Store;
import com.migrosone.courier_tracking.infrastructure.repository.JpaStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreRepositoryAdapterTest {

    @Mock
    private JpaStoreRepository jpaStoreRepository;

    @InjectMocks
    private StoreRepositoryAdapter storeRepositoryAdapter;

    private List<Store> testStores;
    private Store store1, store2;

    @BeforeEach
    void setUp() {
        store1 = new Store("Ataşehir MMM Migros", 40.9923307, 29.1244229);
        store1.setId(1L);
        
        store2 = new Store("Novada MMM Migros", 41.0082376, 28.7784296);
        store2.setId(2L);
        
        testStores = Arrays.asList(store1, store2);
    }

    @Test
    void loadStores_ShouldSaveStoresToJpaRepository() {
        storeRepositoryAdapter.loadStores(testStores);

        verify(jpaStoreRepository).saveAll(testStores);
    }

    @Test
    void findAll_ShouldReturnAllStores() {
        when(jpaStoreRepository.findAll()).thenReturn(testStores);

        List<Store> result = storeRepositoryAdapter.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(store1));
        assertTrue(result.contains(store2));
        verify(jpaStoreRepository).findAll();
    }

    @Test
    void findByName_ExistingStore_ShouldReturnStore() {
        String storeName = "Ataşehir MMM Migros";
        when(jpaStoreRepository.findByName(storeName)).thenReturn(Optional.of(store1));

        Store result = storeRepositoryAdapter.findByName(storeName);

        assertEquals(store1, result);
        assertEquals(storeName, result.getName());
        verify(jpaStoreRepository).findByName(storeName);
    }

    @Test
    void findByName_NonExistingStore_ShouldReturnNull() {
        String storeName = "Bakırköy Migros";
        when(jpaStoreRepository.findByName(storeName)).thenReturn(Optional.empty());

        Store result = storeRepositoryAdapter.findByName(storeName);

        assertNull(result);
        verify(jpaStoreRepository).findByName(storeName);
    }
}