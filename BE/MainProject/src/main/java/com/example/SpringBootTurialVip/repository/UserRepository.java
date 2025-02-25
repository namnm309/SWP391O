package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
//Class này tương tác với JPA , DBMS
public interface UserRepository extends JpaRepository<User,Long> {

    //JPA sẽ tự động generate query check sự tôn tại field Username với cái param cta truyền vào
    boolean existsByUsername (String username);

    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByVerificationcode(String verificationcode);

    boolean existsByFullnameAndBod(String fullname, Date bod);

    //Tìm trẻ theo ParentID
    List<User> findByParentid(Long parentId);

    //Lấy tất cả list role
    List<User> findByRoles_Name(String roleName);

    // Lấy tất cả `Child` (User có parent_id != NULL)
    List<User> findByParentidIsNotNull();

    // Lấy tất cả `Parent` (User có parent_id = NULL)
    List<User> findByParentidIsNull();

    //Tìm user = resest token để change password
    public User findByResetToken(String token);





}
