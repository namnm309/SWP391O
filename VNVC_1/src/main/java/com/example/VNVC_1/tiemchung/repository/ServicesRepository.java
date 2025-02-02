package com.example.VNVC_1.tiemchung.repository;

import com.example.VNVC_1.tiemchung.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long>
{
}
