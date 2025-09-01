package com.migrosone.courier_tracking.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierLocationRequest {
    private LocalDateTime time;
    private String courierId;
    private Double lat;
    private Double lng;
}