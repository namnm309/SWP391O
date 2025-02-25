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
    @ManyToMany//(mappedBy = "roles")//ánh xạ role ngược lại với user , có thể lấy list user thuộc role nào đó
//    @JoinTable(
//            name = "tbl_roles_permissions", // Bảng trung gian
//            joinColumns = @JoinColumn(name = "role_role_name"), // Liên kết với Role
//            inverseJoinColumns = @JoinColumn(name = "permissions_permission_name") // Liên kết với Permission
//    )
    Set<Permission> permissions;
}
