package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.entity.UserRelationship;
import com.example.SpringBootTurialVip.enums.RelativeType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ChildResponse {
    private Long childId;
    private String fullname;
    private LocalDate birthDate;
    private String gender;
    private Double height;
    private Double weight;
    private List<RelativeResponse> relatives;

    // Constructor đơn giản: gán mặc định 1 relative type
    public ChildResponse(User child, RelativeType type) {
        this.childId = child.getId();
        this.fullname = child.getFullname();
        this.birthDate = child.getBod();
        this.gender = child.getGender();
        this.height = child.getHeight();
        this.weight = child.getWeight();
        this.avatarUrl = child.getAvatarUrl();

        // Gán 1 relative mặc định (chính user hiện tại)
       this.relatives = List.of(new RelativeResponse(null, null, type));
    }


    private String avatarUrl;

    // Constructor đầy đủ dữ liệu
    public ChildResponse(User child, List<UserRelationship> relationships) {
        this.childId = child.getId();
        this.fullname = child.getFullname();
        this.birthDate = child.getBod();
        this.gender = child.getGender();
        this.height = child.getHeight();
        this.weight = child.getWeight();
        this.avatarUrl=child.getAvatarUrl();

        // Map danh sách quan hệ sang RelativeResponse
//        this.relatives = relationships.stream()
//                .map(rel -> new RelativeResponse(
//                        rel.getRelative().getId(),
//                        rel.getRelative().getFullname(),
//                        rel.getRelationshipType()
//                ))
//                .toList();
        // **Map danh sách quan hệ sang RelativeResponse**
        if (relationships != null) {
            this.relatives = relationships.stream()
                    .map(rel -> new RelativeResponse(
                            rel.getRelative().getId(),
                            rel.getRelative().getFullname(),
                            rel.getRelationshipType()
                    ))
                    .collect(Collectors.toList());
        } else {
            this.relatives = new ArrayList<>(); // Tránh null pointer
        }
    }

    public ChildResponse(Long id, String fullname, LocalDate bod, String gender, String avatarUrl) {
        this.childId = id;
        this.fullname = fullname;
        this.birthDate = bod;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
    }


    public static class ChildWithInjectionInfoResponse {
        private Long id;
        private String fullname;
        private LocalDate birthDate;
        private String gender;
        private Double height;
        private Double weight;

        private List<InjectionInfo> injections;

        @Getter @Setter
        public static class InjectionInfo {
            private String vaccineTitle;
            private LocalDateTime vaccinationDate;
            private String reactionNote; // Ghi nhận phản ứng sau tiêm
        }
    }




//    public ChildResponse(User child, List<UserRelationship> relationships) {
//    }


}
