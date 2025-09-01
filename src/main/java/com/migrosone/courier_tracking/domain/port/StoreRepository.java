package com.migrosone.courier_tracking.domain.port;

import com.migrosone.courier_tracking.domain.model.Store;

import java.util.List;

public interface StoreRepository {
    List<Store> findAll();
    Store findByName(String name);
}