package com.migrosone.courier_tracking.infrastructure.repository;

import com.migrosone.courier_tracking.domain.model.StoreEntrance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaStoreEntranceRepository extends JpaRepository<StoreEntrance, Long> {

    List<StoreEntrance> findByCourier_IdOrderByEntranceTime(String courierId);
    
    @Query("SELECT s FROM StoreEntrance s WHERE s.courier.id = :courierId AND s.storeName = :storeName ORDER BY s.entranceTime DESC LIMIT 1")
    Optional<StoreEntrance> findLastEntranceByCourierIdAndStoreName(@Param("courierId") String courierId, @Param("storeName") String storeName);
}