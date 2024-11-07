package com.example.demo.controller;

import com.example.demo.entity.LoginLog;
import com.example.demo.entity.User;
import com.example.demo.repository.LoginLogRepository;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final LoginLogRepository loginLogRepository;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, LoginLogRepository loginLogRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.loginLogRepository = loginLogRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
        }
        userService.save(user);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request) {
        String token = userService.login(user.getEmail(), user.getPassword());
        Long userId = userService.findUserIdByEmail(user.getEmail());

        if (token != null && userId != null) {
            User loggedInUser = userService.findById(userId);

            // 클라이언트 IP 주소 추출
            String clientIp = request.getHeader("X-Forwarded-For");
            if (clientIp == null || clientIp.isEmpty()) {
                clientIp = request.getRemoteAddr();
            }

            // LoginLog 생성 및 저장
            LoginLog loginLog = new LoginLog();
            loginLog.setUser(loggedInUser);
            loginLog.setIpAddress(clientIp);
            loginLog.setLoginTime(LocalDateTime.now());
            loginLogRepository.save(loginLog);

            // 응답에 토큰 및 userId 포함
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", userId);
            return ResponseEntity.ok(response); // JSON 형식으로 반환
        } else {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
    }
}