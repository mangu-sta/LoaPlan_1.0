package com.example.loaplan.global.login.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JoinDTO {

    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;
}