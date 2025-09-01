package com.migrosone.courier_tracking.infrastructure.repository;

import com.migrosone.courier_tracking.domain.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCourierRepository extends JpaRepository<Courier, String> {
}