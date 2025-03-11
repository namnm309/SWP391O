package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.entity.UserRelationship;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ChildResponse {
    private Long userId;
    private String fullname;
    private Date birthDate;
    private String gender;
    private Double height;
    private Double weight;
    private List<RelativeResponse> relatives;
    private String avatarUrl;

    // Constructor đầy đủ dữ liệu
    public ChildResponse(User child, List<UserRelationship> relationships) {
        this.userId = child.getId();
        this.fullname = child.getFullname();
        this.birthDate = child.getBod();
        this.gender = child.getGender();
        this.height = child.getHeight();
        this.weight = child.getWeight();
        this.avatarUrl=child.getAvatarUrl();

        // Map danh sách quan hệ sang RelativeResponse
        this.relatives = relationships.stream()
                .map(rel -> new RelativeResponse(
                        rel.getRelative().getId(),
                        rel.getRelative().getFullname(),
                        rel.getRelationshipType()
                ))
                .toList();
    }

//    public ChildResponse(User child, List<UserRelationship> relationships) {
//    }


}
