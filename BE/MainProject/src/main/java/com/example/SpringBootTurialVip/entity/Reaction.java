package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import jdk.jfr.Registered;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@Entity
@Table(name = "tbl_reaction")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private User child;  // Trẻ được ghi nhận phản ứng

    @Column(nullable = false, length = 1000)
    private String symptoms;

    @Column(nullable = false)
    private LocalDateTime reportedAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;  // Người viết phản ứng (staff hoặc customer)

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;  // Người cập nhật phản ứng


}
