package com.migrosone.courier_tracking.domain.port;

import com.migrosone.courier_tracking.domain.model.StoreEntrance;

import java.util.List;
import java.util.Optional;

public interface StoreEntranceRepository {
    void save(StoreEntrance entrance);
    List<StoreEntrance> findByCourierId(String courierId);
    Optional<StoreEntrance> findLastEntranceByCourierIdAndStoreName(String courierId, String storeName);
}