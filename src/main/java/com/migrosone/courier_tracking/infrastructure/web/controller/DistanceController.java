package com.migrosone.courier_tracking.infrastructure.web.controller;

import com.migrosone.courier_tracking.application.usecase.GetTotalTravelDistanceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courier")
@RequiredArgsConstructor
@Slf4j
public class DistanceController {
    
    private final GetTotalTravelDistanceUseCase getTotalTravelDistanceUseCase;
    
    @GetMapping("/{courierId}/total-distance")
    public ResponseEntity<Double> getTotalTravelDistance(@PathVariable String courierId) {
        try {
            Double totalDistance = getTotalTravelDistanceUseCase.getTotalTravelDistance(courierId);
            return ResponseEntity.ok(totalDistance);
        } catch (Exception e) {
            log.error("Error getting total travel distance for courier: {}", courierId, e);
            return ResponseEntity.badRequest().build();
        }
    }
}