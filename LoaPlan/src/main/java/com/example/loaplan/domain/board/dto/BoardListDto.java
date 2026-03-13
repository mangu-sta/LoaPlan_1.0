package com.example.loaplan.domain.board.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String category;
    private List<String> imageUrls;
    private int commentCount;
    private int likeCount;
    private int viewCount;
    private LocalDateTime createdAt;
}
