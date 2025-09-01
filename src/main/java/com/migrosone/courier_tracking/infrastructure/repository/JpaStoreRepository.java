package com.migrosone.courier_tracking.infrastructure.repository;

import com.migrosone.courier_tracking.domain.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaStoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
}