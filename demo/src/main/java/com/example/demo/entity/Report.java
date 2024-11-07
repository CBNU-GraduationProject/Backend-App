package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User 객체와의 관계 설정

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Lob
    private byte[] image; // 이미지 파일을 BLOB으로 저장

    private double latitude; // 위도
    private double longitude; // 경도

    @Column(nullable = false, length = 50)
    private String state = "미조치"; // state 필드 기본값 설정

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
