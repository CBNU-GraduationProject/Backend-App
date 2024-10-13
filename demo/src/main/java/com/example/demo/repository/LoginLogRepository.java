package com.example.demo.repository;

import com.example.demo.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    // 특정 사용자의 로그인 기록 조회
    List<LoginLog> findByUserId(Long userId);
}
