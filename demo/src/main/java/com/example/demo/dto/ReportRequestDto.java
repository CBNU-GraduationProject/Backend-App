package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {
    private Long id;
    private String description;
    private Double latitude;
    private Double longitude;
    private byte[] image; // or set it as a URL if stored elsewhere
    private LocalDateTime createdAt;
    private String state;
    private UserDto user;

    public ReportRequestDto(Long id, String description, Double latitude, Double longitude, byte[] image, LocalDateTime createdAt, String state) {
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.createdAt = createdAt;
        this.state = state;
    }

    @Getter
    public static class UserDto {
        private String email;

        public UserDto() {}

        @JsonCreator
        public UserDto(@JsonProperty("email") String email) {
            this.email = email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
