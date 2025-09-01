package com.migrosone.courier_tracking.infrastructure.web.controller;

import com.migrosone.courier_tracking.application.usecase.ProcessCourierLocationUseCase;
import com.migrosone.courier_tracking.infrastructure.web.dto.CourierLocationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courier-location")
@RequiredArgsConstructor
@Slf4j
public class CourierLocationController {
    
    private final ProcessCourierLocationUseCase processCourierLocationUseCase;
    
    @PostMapping
    public ResponseEntity<String> submitCourierLocation(@RequestBody CourierLocationRequest request) {
        try {
            processCourierLocationUseCase.processLocation(
                request.getCourierId(),
                request.getLat(),
                request.getLng(),
                request.getTime()
            );
            return ResponseEntity.ok("Location processed successfully");
        } catch (Exception e) {
            log.error("Error processing courier location", e);
            return ResponseEntity.badRequest().body("Error processing location: " + e.getMessage());
        }
    }
}