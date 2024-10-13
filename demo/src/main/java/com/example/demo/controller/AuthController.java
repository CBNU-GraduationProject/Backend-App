package com.example.demo.controller;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.SignupRequestDto;
import com.example.demo.entity.User;
import com.example.demo.service.JwtTokenProvider;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService; // 사용자 서비스 추가

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(), loginRequestDto.getPassword()
                )
        );
        String token = jwtTokenProvider.generateToken(authentication.getName());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        // 사용자 중복 체크
        if (userService.existsByEmail(signupRequestDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        // 새로운 사용자 생성 및 저장
        User newUser = new User();
        newUser.setName(signupRequestDto.getName());
        newUser.setEmail(signupRequestDto.getEmail());
        newUser.setPassword(signupRequestDto.getPassword()); // 비밀번호는 암호화하여 저장하는 것이 좋음

        userService.save(newUser); // 사용자 저장 로직

        return ResponseEntity.ok("User registered successfully");
    }
}
