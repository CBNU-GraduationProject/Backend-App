/*package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude; // 위도
    private double longitude; // 경도

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    // 생성자에서 createdAt 초기화
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}*/
