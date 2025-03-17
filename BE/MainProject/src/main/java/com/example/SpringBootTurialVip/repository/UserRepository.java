package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    boolean existsByFullnameAndBod(String fullname, LocalDate bod);

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    User findByIdDirect(@Param("userId") Long userId);

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

    //Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createAt >= :startDate")
    long countNewCustomersSince(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(u) FROM User u")
    long countTotalCustomers();


    @Query(value = """
    SELECT DATE(create_at) AS date, COUNT(*) AS totalCustomers
    FROM tbl_users
    WHERE create_at >= CURRENT_DATE - (? * INTERVAL '1 day')
    GROUP BY date
    ORDER BY date ASC
    """, nativeQuery = true)
    List<Object[]> getDailyNewCustomers(@Param("days") int days);


    Optional<User> findById(Long userId);


}


