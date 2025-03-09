package com.example.SpringBootTurialVip.entity;

import com.example.SpringBootTurialVip.enums.RelativeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user_relationship")
public class UserRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id", referencedColumnName = "user_id", nullable = false)
    private User child;  // Trẻ em

    @ManyToOne
    @JoinColumn(name = "relative_id", referencedColumnName = "user_id", nullable = false)
    private User relative;  // Người thân

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    private RelativeType relationshipType;  // Kiểu quan hệ cố định

    public UserRelationship(User child, User parent, RelativeType relationshipType) {
    }

//    public UserRelationship() {}
//
//    public UserRelationship(User child, User relative, RelativeType relationshipType) {
//        this.child = child;
//        this.relative = relative;
//        this.relationshipType = relationshipType;
//    }


}

