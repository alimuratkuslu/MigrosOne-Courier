package com.migrosone.courier_tracking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Distance {

    private double meters;
    
    public Distance add(Distance other) {
        return new Distance(this.meters + other.meters);
    }
    
    public boolean isWithinRadius(double radiusMeters) {
        return this.meters <= radiusMeters;
    }
}