package com.migrosone.courier_tracking.domain.service;

import com.migrosone.courier_tracking.domain.model.StoreEntrance;

public interface StoreEntranceObserver {
    void onStoreEntrance(StoreEntrance entrance);
}