package com.migrosone.courier_tracking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "store_entrances")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreEntrance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;
    
    @Column(name = "store_name", nullable = false)
    private String storeName;
    
    @Column(name = "entrance_time", nullable = false)
    private LocalDateTime entranceTime;

    public StoreEntrance(Courier courier, String storeName, LocalDateTime entranceTime) {
        this.courier = courier;
        this.storeName = storeName;
        this.entranceTime = entranceTime;
    }

    public boolean isWithinOneMinuteOf(LocalDateTime otherTime) {
        return Math.abs(java.time.Duration.between(this.entranceTime, otherTime).toSeconds()) < 60;
    }
    
    public String getCourierId() {
        return courier != null ? courier.getId() : null;
    }
}