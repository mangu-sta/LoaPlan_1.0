package com.example.loaplan.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String nickname;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER; // 기본 USER

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role {
        USER, ADMIN
    }
}