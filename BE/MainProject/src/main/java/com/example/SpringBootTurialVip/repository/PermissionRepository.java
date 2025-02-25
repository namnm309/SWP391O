package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

}
