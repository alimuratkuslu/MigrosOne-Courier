package com.migrosone.courier_tracking.infrastructure.adapter;

import com.migrosone.courier_tracking.domain.port.StoreEntranceRepository;
import com.migrosone.courier_tracking.domain.model.StoreEntrance;
import com.migrosone.courier_tracking.infrastructure.repository.JpaStoreEntranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StoreEntranceRepositoryAdapter implements StoreEntranceRepository {
    
    private final JpaStoreEntranceRepository jpaStoreEntranceRepository;
    
    @Override
    public void save(StoreEntrance entrance) {
        jpaStoreEntranceRepository.save(entrance);
    }
    
    @Override
    public List<StoreEntrance> findByCourierId(String courierId) {
        return jpaStoreEntranceRepository.findByCourier_IdOrderByEntranceTime(courierId);
    }
    
    @Override
    public Optional<StoreEntrance> findLastEntranceByCourierIdAndStoreName(String courierId, String storeName) {
        return jpaStoreEntranceRepository.findLastEntranceByCourierIdAndStoreName(courierId, storeName);
    }
}