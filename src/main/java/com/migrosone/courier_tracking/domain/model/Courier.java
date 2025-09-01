package com.migrosone.courier_tracking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "couriers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Courier {
    
    @Id
    private String id;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;
    
    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CourierLocation> locations = new ArrayList<>();
    
    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StoreEntrance> storeEntrances = new ArrayList<>();

    public Courier(String id) {
        this.id = id;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.locations = new ArrayList<>();
        this.storeEntrances = new ArrayList<>();
    }
}