package com.example.VNVC_1.tiemchung.repository;

import com.example.VNVC_1.tiemchung.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>
{
    Optional<Users> findByUsername(String username);
}
