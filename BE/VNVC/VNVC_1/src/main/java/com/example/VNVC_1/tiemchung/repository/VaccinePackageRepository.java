package com.example.VNVC_1.tiemchung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.VNVC_1.tiemchung.model.VaccinePackage;

@Repository
public interface VaccinePackageRepository extends JpaRepository<VaccinePackage, Long> {
    // Không cần khai báo thêm phương thức vì JpaRepository đã có sẵn các CRUD method
}
