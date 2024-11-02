package com.example.demo.controller;

import com.example.demo.dto.ReportDto;
import com.example.demo.entity.Report;
import com.example.demo.entity.User;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        User user = userRepository.findByEmail(report.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        report.setUser(user);
        return reportRepository.save(report);
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<ReportDto>> getReportsByUserEmail(@PathVariable String userEmail) {
        List<Report> reports = reportRepository.findByUserEmail(userEmail);
        List<ReportDto> reportDtos = reports.stream()
                .map(report -> new ReportDto(report.getId(), report.getDescription(), report.getLatitude(), report.getLongitude()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportDtos);
    }
}

