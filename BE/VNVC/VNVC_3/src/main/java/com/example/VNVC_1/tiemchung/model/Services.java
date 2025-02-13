package com.example.VNVC_1.tiemchung.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "tbl_services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(name = "service_name", nullable = false, unique = true)
    private String serviceName;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServicesType type;

    @Column(nullable = false)
    private Double price;

    @CreationTimestamp
    @Column(name = "date_create", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreate;

    @UpdateTimestamp
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    // Constructor không tham số (Mặc định)
    public Services() {
    }

    // Constructor có tham số
    public Services(String serviceName, String description, ServicesType type, Double price) {
        this.serviceName = serviceName;
        this.description = description;
        this.type = type;
        this.price = price;
    }

    //  Getter và Setter thủ công

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServicesType getType() {
        return type;
    }

    public void setType(ServicesType type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Giá dịch vụ không được nhỏ hơn 0!");
        }
        this.price = price;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    //  Ghi đè phương thức toString()
    @Override
    public String toString() {
        return "Services{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", dateCreate=" + dateCreate +
                ", dateUpdate=" + dateUpdate +
                '}';
    }
}
