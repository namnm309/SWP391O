package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

//    @ManyToMany
//    @JoinTable(
//            name = "tbl_roles_permissions",
//            joinColumns = @JoinColumn(name = "permissions_permission_name"),
//            inverseJoinColumns = @JoinColumn(name = "role_role_name")
//    )
//    private Set<Role> roles;
}
