package com.example.SpringBootTurialVip.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

//@Data

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    String name;
    String description;
}
