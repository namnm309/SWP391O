package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
