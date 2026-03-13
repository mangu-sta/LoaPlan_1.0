package com.example.loaplan.domain.board.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long boardId;
    private Long parentId;
    private String parentNickname; // ⭐ 부모 댓글 작성자 닉네임 (대댓글 태그용)

    private String nickname;
    private String profileImage; // 캐릭터 이미지 or 기본 이미지

    private String content;
    private String createdAt;

    private boolean deleted;

    private List<CommentDto> replies;
}
