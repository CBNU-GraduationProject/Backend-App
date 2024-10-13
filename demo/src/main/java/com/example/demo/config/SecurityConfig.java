package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 비밀번호 암호화용 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 사용자 인증을 위한 UserDetailsService 설정
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
    }

    // Security 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (필요 시 적절히 수정)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup", "/resources/**").permitAll()  // 인증 불필요한 경로 설정
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 커스텀 로그인 페이지 경로 설정
                        .defaultSuccessUrl("/mypage", true)  // 로그인 성공 시 이동 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 경로 설정
                        .logoutSuccessUrl("/login")  // 로그아웃 성공 후 이동 경로
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID")  // 쿠키 삭제
                        .permitAll()
                );

        return http.build();
    }
}
