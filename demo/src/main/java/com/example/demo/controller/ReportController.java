package com.example.demo.controller;

import com.example.demo.dto.ReportRequestDto;
import com.example.demo.entity.Report;
import com.example.demo.entity.User;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;
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
    public Report createReport(@RequestBody ReportRequestDto reportRequestDto) {
        User user = userRepository.findByEmail(reportRequestDto.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = new Report();
        report.setUser(user);
        report.setDescription(reportRequestDto.getDescription());
        report.setLatitude(reportRequestDto.getLatitude());
        report.setLongitude(reportRequestDto.getLongitude());
        report.setState(reportRequestDto.getState() != null ? reportRequestDto.getState() : "미조치");

        if (reportRequestDto.getImage() != null) {
            report.setImage(Base64.getDecoder().decode(reportRequestDto.getImage()));
        }

        return reportRepository.save(report);
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<ReportRequestDto>> getReportsByUserEmail(@PathVariable String userEmail) {
        List<Report> reports = reportRepository.findByUserEmail(userEmail);
        List<ReportRequestDto> reportDtos = reports.stream()
                .map(report -> new ReportRequestDto(
                        report.getId(),
                        report.getDescription(),
                        report.getLatitude(),
                        report.getLongitude(),
                        report.getImage(),
                        report.getCreatedAt(),
                        report.getState()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportDtos);
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<Report> updateReportState(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        String state = body.get("state"); // 요청 본문에서 state 값을 추출

        if (state != null) {
            report.setState(state);
            reportRepository.save(report);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(report);
    }

}
