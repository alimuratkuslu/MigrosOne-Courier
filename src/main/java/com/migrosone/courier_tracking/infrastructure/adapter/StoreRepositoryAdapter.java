package com.migrosone.courier_tracking.infrastructure.adapter;

import com.migrosone.courier_tracking.domain.port.StoreRepository;
import com.migrosone.courier_tracking.domain.model.Store;
import com.migrosone.courier_tracking.infrastructure.repository.JpaStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreRepositoryAdapter implements StoreRepository {
    
    private final JpaStoreRepository jpaStoreRepository;
    
    public void loadStores(List<Store> storeList) {
        jpaStoreRepository.saveAll(storeList);
    }
    
    @Override
    public List<Store> findAll() {
        return jpaStoreRepository.findAll();
    }
    
    @Override
    public Store findByName(String name) {
        return jpaStoreRepository.findByName(name).orElse(null);
    }
}