package com.example.loaplan.domain.board.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private int viewCount;
    private int commentCount;
    private int likeCount;
}

