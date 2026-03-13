package com.example.loaplan.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoinDTO {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
