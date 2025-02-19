package com.example.VNVC_1.tiemchung.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vaccine_packages")
public class VaccinePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String packageName;
    private Double price;
}
