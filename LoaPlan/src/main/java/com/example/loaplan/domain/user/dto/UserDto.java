package com.example.loaplan.domain.user.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDto {
    private Long id;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;
}