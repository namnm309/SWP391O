package com.example.SpringBootTurialVip.dto.request;

import com.example.SpringBootTurialVip.entity.Role;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StaffUpdateRequest {
//     existingStaff.setFullname(staff.getFullname());
//        existingStaff.setEmail(staff.getEmail());
//        existingStaff.setPhone(staff.getPhone());
    private String fullname;
    private boolean enabled;
    private Set<Role> roles;

}
