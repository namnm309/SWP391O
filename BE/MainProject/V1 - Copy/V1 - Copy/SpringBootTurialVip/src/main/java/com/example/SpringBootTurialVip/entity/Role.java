package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="tbl_roles")
@Entity
public class Role {
    @Id
    @Column(name="role_name")
    private String name;
    @Column(name="role_description")
    private String description;

    //Quan hệ với bảng permission
    @ManyToMany
    Set<Permission> permissions;
}
