package com.example.SpringBootTurialVip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCreationRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Full name cannot be blank")
    private String fullname;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(min = 10, max = 15, message = "Invalid phone number")
    private String phone;

    private LocalDate bod;
    private String gender;
}
