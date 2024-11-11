package com.example.demo.controller;

import com.example.demo.dto.ReportRequestDto;
import com.example.demo.entity.Report;
import com.example.demo.entity.User;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@CrossOrigin(
        origins = {
                "https://kickx2-frontend-b2evfnacdxh3fjd4.koreacentral-01.azurewebsites.net",
                "https://kickx2-backend-ezgpc2fyhceae4ev.koreacentral-01.azurewebsites.net"
        },
        allowedHeaders = "*",
        exposedHeaders = "Authorization"
)

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${hazarddata.service.url}") // HazardData 서비스 URL
    private String hazardDataServiceUrl;

    public ReportController(ReportRepository reportRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveReport(@PathVariable Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("승인"); // 승인 상태로 변경
        reportRepository.save(report); // 상태 업데이트

        MultiValueMap<String, Object> formData = getStringObjectMultiValueMap(report);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

            String url = hazardDataServiceUrl + "/api/hazarddata/add";
            restTemplate.postForObject(url, requestEntity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add report to hazard data service");
        }

        return ResponseEntity.ok("Report approved and added to hazard data!");
    }

    // 기존 메서드에서 userid 값 추가
    private static MultiValueMap<String, Object> getStringObjectMultiValueMap(Report report) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("hazardType", report.getDescription());
        formData.add("userid", report.getUser().getId().toString()); // 수정된 부분
        formData.add("gps", report.getLatitude() + "," + report.getLongitude());
        // LocalDateTime을 String으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = report.getCreatedAt().format(formatter);
        formData.add("dates", formattedDate);
        formData.add("state", "미조치");

        if (report.getImage() != null) {
            // 이미지 파일을 byte[]로 변환
            ByteArrayResource imageResource = new ByteArrayResource(report.getImage()) {
                @Override
                public String getFilename() {
                    return "image.jpg"; // 이미지 파일 이름 지정
                }
            };
            formData.add("photo", imageResource);
        }
        return formData;
    }


    @PostMapping
    public ResponseEntity<ReportRequestDto> createReport(
            @RequestParam("description") String description,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam("userEmail") String userEmail,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = new Report();
        report.setUser(user);
        report.setDescription(description);
        report.setLatitude(latitude);
        report.setLongitude(longitude);
        report.setState(state != null ? state : "미조치");

        if (image != null && !image.isEmpty()) {
            report.setImage(image.getBytes());
        }

        Report savedReport = reportRepository.save(report);
        return ResponseEntity.ok(convertToDto(savedReport));
    }

    @CrossOrigin(origins = "https://kickx2-frontend-b2evfnacdxh3fjd4.koreacentral-01.azurewebsites.net/")
    @GetMapping
    public ResponseEntity<List<ReportRequestDto>> getAllReports() {
        List<Report> reports = reportRepository.findByStatus("반려"); // 반려 상태인 내역만 조회
        List<ReportRequestDto> reportDtos = reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportDtos);
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<ReportRequestDto>> getReportsByUserEmail(@PathVariable String userEmail) {
        List<Report> reports = reportRepository.findByUserEmail(userEmail);
        List<ReportRequestDto> reportDtos = reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportDtos);
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<ReportRequestDto> updateReportState(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        String state = body.get("state");
        if (state != null) {
            report.setState(state);
            reportRepository.save(report);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(convertToDto(report));
    }


    // 삭제 기능 추가: 반려된 보고서를 삭제하는 API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> rejectReport(@PathVariable Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        reportRepository.delete(report); // 반려 상태로 삭제
        return ResponseEntity.ok("Report rejected and deleted!");
    }
    private ReportRequestDto convertToDto(Report report) {
        return new ReportRequestDto(
                report.getId(),
                report.getDescription(),
                report.getLatitude(),
                report.getLongitude(),
                report.getImage(),
                report.getCreatedAt(),
                report.getState(),
                new ReportRequestDto.UserDto(report.getUser().getEmail())
        );
    }
}
