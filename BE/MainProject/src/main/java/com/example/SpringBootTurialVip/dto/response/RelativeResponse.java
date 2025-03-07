package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.enums.RelativeType;
import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@Setter
//@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class RelativeResponse {
    private Long relativeId;
    private String fullname;
    private RelativeType relationshipType;  // Enum quan hệ

//    public RelativeResponse(Long id, String fullname, RelativeType relationshipType) {
//    }
}
