package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByName(String name);

    Optional<Permission> findByName(String name);

}
