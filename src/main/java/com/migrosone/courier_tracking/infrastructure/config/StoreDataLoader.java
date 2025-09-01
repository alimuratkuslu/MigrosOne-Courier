package com.migrosone.courier_tracking.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosone.courier_tracking.domain.model.Store;
import com.migrosone.courier_tracking.infrastructure.adapter.StoreRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoreDataLoader {
    
    private final StoreRepositoryAdapter storeRepositoryAdapter;
    private final ObjectMapper objectMapper;
    
    @PostConstruct
    public void loadStoresOnStartup() {
        loadStores();
    }

    // Load Stores to DB
    private void loadStores() {
        try {
            ClassPathResource resource = new ClassPathResource("stores.json");
            InputStream inputStream = resource.getInputStream();
            
            List<Map<String, Object>> storeData = objectMapper.readValue(
                inputStream, 
                new TypeReference<List<Map<String, Object>>>() {}
            );
            
            List<Store> stores = storeData.stream()
                .map(this::mapToStore)
                .collect(Collectors.toList());
            
            storeRepositoryAdapter.loadStores(stores);
            
        } catch (IOException e) {
            throw new RuntimeException("Could not load store data", e);
        }
    }
    
    private Store mapToStore(Map<String, Object> storeMap) {
        String name = (String) storeMap.get("name");
        double lat = ((Number) storeMap.get("lat")).doubleValue();
        double lng = ((Number) storeMap.get("lng")).doubleValue();
        
        return new Store(name, lat, lng);
    }
}