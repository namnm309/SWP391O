package com.example.VNVC_1.tiemchung.model;

import com.example.VNVC_1.tiemchung.model.ServicesType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "tbl_services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    private String description;

    @Enumerated(EnumType.STRING)
    private ServicesType type;

    @Column(nullable = false)
    private Double price;

    @Column(name = "date_create")
    @Temporal(TemporalType.DATE)
    private Date dateCreate;
}


