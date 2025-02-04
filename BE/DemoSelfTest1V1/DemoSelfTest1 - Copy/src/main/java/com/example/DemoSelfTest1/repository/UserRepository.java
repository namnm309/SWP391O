package com.example.DemoSelfTest1.repository;

import com.example.DemoSelfTest1.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByEmail(String email); //
    Optional<User> findByUsername(String username);
    //Tìm người dùng = Verifi code để xác minh có phải chính xác mã này dành cho user đó ko
    Optional<User> findByVerificationCode(String verificationCode);

}
