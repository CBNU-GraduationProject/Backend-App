package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Base64;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {
    private Long id;
    private String description; // 신고 내용
    private Double latitude; // 위도
    private Double longitude; // 경도
    private String image; // Base64로 인코딩된 이미지
    private LocalDateTime createdAt; // 신고 날짜
    private UserDto user; // 사용자 정보 추가

    public ReportRequestDto(Long id, String description, Double latitude, Double longitude, byte[] image, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image != null ? Base64.getEncoder().encodeToString(image) : null; // imageBase64 사용
        this.createdAt = createdAt;
    }

    @Data
    public static class UserDto {
        private String email;
    }
}
