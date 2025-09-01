package com.migrosone.courier_tracking.domain.port;

import com.migrosone.courier_tracking.domain.model.CourierLocation;

import java.util.List;

public interface CourierLocationRepository {
    void save(CourierLocation location);
    List<CourierLocation> findByCourierId(String courierId);
    CourierLocation findLastLocationByCourierId(String courierId);
}