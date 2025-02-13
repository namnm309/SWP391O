package com.example.SpringBootTurialVip.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {
    String name;
    String description;
}
