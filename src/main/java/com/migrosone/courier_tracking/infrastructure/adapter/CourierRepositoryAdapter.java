package com.migrosone.courier_tracking.infrastructure.adapter;

import com.migrosone.courier_tracking.domain.port.CourierRepository;
import com.migrosone.courier_tracking.domain.model.Courier;
import com.migrosone.courier_tracking.infrastructure.repository.JpaCourierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CourierRepositoryAdapter implements CourierRepository {
    
    private final JpaCourierRepository jpaCourierRepository;
    
    @Override
    public void save(Courier courier) {
        jpaCourierRepository.save(courier);
    }
    
    @Override
    public Optional<Courier> findById(String id) {
        return jpaCourierRepository.findById(id);
    }
    
    @Override
    public boolean existsById(String id) {
        return jpaCourierRepository.existsById(id);
    }
}