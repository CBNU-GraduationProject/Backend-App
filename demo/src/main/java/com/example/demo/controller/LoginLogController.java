package com.example.demo.controller;

import com.example.demo.entity.LoginLog;
import com.example.demo.repository.LoginLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login-logs")
public class LoginLogController {

    private final LoginLogRepository loginLogRepository;

    public LoginLogController(LoginLogRepository loginLogRepository) {
        this.loginLogRepository = loginLogRepository;
    }

    @PostMapping
    public ResponseEntity<LoginLog> createLoginLog(@RequestBody LoginLog loginLog) {
        LoginLog savedLog = loginLogRepository.save(loginLog);
        return ResponseEntity.ok(savedLog);
    }

    @GetMapping
    public ResponseEntity<List<LoginLog>> getAllLoginLogs() {
        return ResponseEntity.ok(loginLogRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoginLog> getLoginLogById(@PathVariable Long id) {
        return loginLogRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
