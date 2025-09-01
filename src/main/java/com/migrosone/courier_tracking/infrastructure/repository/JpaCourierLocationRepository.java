package com.migrosone.courier_tracking.infrastructure.repository;

import com.migrosone.courier_tracking.domain.model.CourierLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCourierLocationRepository extends JpaRepository<CourierLocation, Long> {

    List<CourierLocation> findByCourier_IdOrderByTimestamp(String courierId);
    
    @Query("SELECT c FROM CourierLocation c WHERE c.courier.id = :courierId ORDER BY c.timestamp DESC LIMIT 1")
    Optional<CourierLocation> findLastLocationByCourierId(@Param("courierId") String courierId);
}