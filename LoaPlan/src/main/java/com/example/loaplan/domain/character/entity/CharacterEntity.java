package com.example.loaplan.domain.character.entity;
import com.example.loaplan.domain.user.entity.UserEntity;
import jakarta.persistence.*; import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`character`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CharacterEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ 유저와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 100)
    private String nickname;

    private String serverName;
    private String className;

    @Column(precision = 10, scale = 2)
    private BigDecimal itemLevel;

    @Column(length = 500)
    private String characterImageUrl;

    // ✅ 새로 추가된 필드: 캐릭터 순서 (정렬용)
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 0; // 기본값 0

    // ✅ 자동으로 수정시간 갱신
    @CreationTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();
}