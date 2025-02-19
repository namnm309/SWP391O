package com.example.VNVC_1.tiemchung.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "tbl_vaccines")
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vaccine_name", nullable = false, length = 100)
    private String vaccineName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 255)
    private String description;

    @Column(name = "vaccine_age", length = 50)
    private String vaccineAge;

    @Column(name = "date_of_manufacture", nullable = false)
    private Date dateOfManufacture;

    @Column(name = "vaccine_expiry_date")
    private String vaccineExpiryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVaccineAge() {
        return vaccineAge;
    }

    public void setVaccineAge(String vaccineAge) {
        this.vaccineAge = vaccineAge;
    }

    public Date getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(Date dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    public String getVaccineExpiryDate() {
        return vaccineExpiryDate;
    }

    public void setVaccineExpiryDate(String vaccineExpiryDate) {
        this.vaccineExpiryDate = vaccineExpiryDate;
    }

    //xài bth nó báo vàng do lombok khả duụng
}
