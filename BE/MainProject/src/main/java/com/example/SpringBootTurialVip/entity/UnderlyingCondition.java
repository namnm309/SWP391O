package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_underlying_conditions")
public class UnderlyingCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với User (trẻ em)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Liên kết với User (trẻ em)

    @Column(name="condition_name", length = 500)
    private String conditionName; // Lưu tên bệnh nền dưới dạng String (ví dụ: "Tiểu đường")

    @Column(name="condition_description", length = 1000)
    private String conditionDescription; // Mô tả chi tiết bệnh nền nếu cần
}
