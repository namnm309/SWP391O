package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="tbl_permission")
@Entity
public class Permission {
    @Id
    @Column(name="permission_name")
    private String name;
    @Column(name="permission_description")
    private String description;
}
