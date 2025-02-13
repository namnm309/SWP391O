package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//Class này tương tác với JPA , DBMS
public interface UserRepository extends JpaRepository<User,String> {

    //JPA sẽ tự động generate query check sự tôn tại field Username với cái param cta truyền vào
    boolean existsByUsername (String username);
    Optional<User> findByUsername(String username);
}
