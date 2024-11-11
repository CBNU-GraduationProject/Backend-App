package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class HazardData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hid;

    private String hazardType;

    private String userid;

    @Lob
    @Column(columnDefinition="LONGBLOB")
    private byte[] photo;

    private String gps;
    private String state;
    private LocalDateTime dates;
}
