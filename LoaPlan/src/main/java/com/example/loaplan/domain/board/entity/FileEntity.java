package com.example.loaplan.domain.board.entity;

import com.example.loaplan.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class FileEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String originalName;
    private String storedName;
    private String filePath;
    private String fileType;
    private Long fileSize;

    private boolean isThumbnail;
    private boolean isActive;

    private LocalDateTime uploadedAt;
}
