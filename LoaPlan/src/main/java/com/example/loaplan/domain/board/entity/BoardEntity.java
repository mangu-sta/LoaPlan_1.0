package com.example.loaplan.domain.board.entity;

import com.example.loaplan.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class BoardEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;

    @Lob
    private String content;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "is_notice")
    private boolean isNotice;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @Column(name = "comment_count")
    private int commentCount;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "ip_address")
    private String ipAddress;


}
