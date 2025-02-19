package com.example.VNVC_1.tiemchung.repository;

import com.example.VNVC_1.tiemchung.model.Services;
import com.example.VNVC_1.tiemchung.model.ServicesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long>
{
    Optional<Services> findByType(ServicesType type);
}
