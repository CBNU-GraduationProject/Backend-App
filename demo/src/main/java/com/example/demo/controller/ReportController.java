package com.example.demo.controller;

import com.example.demo.entity.Report;
import com.example.demo.entity.User;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportController(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Report createReport(@RequestBody Report report) {
        // 사용자 이메일로 사용자 찾기
        User user = userRepository.findByEmail(report.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Report 객체에 user를 설정
        report.setUser(user); // 사용자 정보를 Report에 설정

        // 위도 및 경도 설정 (이제 Location 객체가 아닌 값으로 저장)
        report.setLatitude(report.getLatitude());
        report.setLongitude(report.getLongitude());

        // Report 저장
        return reportRepository.save(report);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        return reportRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<Report>> getReportsByUserEmail(@PathVariable String userEmail) {
        return ResponseEntity.ok(reportRepository.findByUserEmail(userEmail));
    }
}
