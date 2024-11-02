package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Long id;
    private String description; // 신고 내용
    private Double latitude; // 위도
    private Double longitude; // 경도
    // 필요한 다른 필드 추가
}
