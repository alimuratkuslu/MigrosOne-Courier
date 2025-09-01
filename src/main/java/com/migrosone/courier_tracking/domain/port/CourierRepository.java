package com.migrosone.courier_tracking.domain.port;

import com.migrosone.courier_tracking.domain.model.Courier;

import java.util.Optional;

public interface CourierRepository {
    void save(Courier courier);
    Optional<Courier> findById(String id);
    boolean existsById(String id);
}