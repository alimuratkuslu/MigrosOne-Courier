package com.migrosone.courier_tracking.domain.service;

import com.migrosone.courier_tracking.domain.model.Distance;

public interface DistanceCalculationStrategy {
    Distance calculateDistance(double fromLat, double fromLng, double toLat, double toLng);
}