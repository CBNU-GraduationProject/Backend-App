package com.example.demo.repository;

import com.example.demo.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // 특정 사용자의 신고 내역 조회
    List<Report> findByUserId(Long userId);
}
