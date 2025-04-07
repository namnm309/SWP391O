package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.ConsultationRequest;
import com.example.SpringBootTurialVip.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRequestRepository
        extends JpaRepository<ConsultationRequest,Long> {

    List<ConsultationRequest> findByStatus(RequestStatus status);
}
