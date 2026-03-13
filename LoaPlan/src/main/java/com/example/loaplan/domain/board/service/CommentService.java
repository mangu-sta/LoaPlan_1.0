package com.example.loaplan.domain.board.service;

import com.example.loaplan.domain.board.dto.CommentDto;
import com.example.loaplan.domain.board.entity.BoardEntity;
import com.example.loaplan.domain.board.entity.CommentEntity;
import com.example.loaplan.domain.board.repository.BoardRepository;
import com.example.loaplan.domain.board.repository.CommentRepository;
import com.example.loaplan.domain.character.service.CharacterService;
import com.example.loaplan.domain.user.entity.UserEntity;
import com.example.loaplan.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final CharacterService characterService; // ⭐ 프로필 이미지 위해 필요

    // ---------------- 댓글 생성 ----------------
    @Transactional
    public CommentDto createComment(CommentDto dto, String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        BoardEntity board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        CommentEntity parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글 없음"));
        }

        CommentEntity saved = commentRepository.save(
                CommentEntity.builder()
                        .board(board)
                        .user(user)
                        .parent(parent)
                        .content(dto.getContent())
                        .createdAt(LocalDateTime.now())
                        .isDeleted(false)
                        .build());

        // 🔥 여기서 게시글의 comment_count +1
        board.setCommentCount(board.getCommentCount() + 1);
        // @Transactional 이라 dirty checking으로 자동 update 됨 (save() 안 해도 됨)

        return toDto(saved);
    }

    // 댓글 목록 (계층은 유지하되 UI는 평면적으로)
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long boardId) {

        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        List<CommentEntity> list = commentRepository.findByBoardAndIsDeletedFalseOrderByIdAsc(board);

        // 1) DTO 변환
        List<CommentDto> dtos = list.stream()
                .map(this::toDto)
                .toList();

        // 2) 완전 평탄화된 flat 구조로 반환
        return buildFlatReplies(dtos);
    }

    private List<CommentDto> buildFlatReplies(List<CommentDto> list) {

        // ID → DTO 매핑
        Map<Long, CommentDto> map = list.stream()
                .collect(Collectors.toMap(CommentDto::getId, c -> c));

        // parent = null → 최상위 댓글
        List<CommentDto> roots = new ArrayList<>();

        for (CommentDto c : list) {

            if (c.getParentId() == null) {
                // root 저장
                roots.add(c);
                continue;
            }

            // 현재 댓글의 "최상위 부모(root)"를 찾기
            CommentDto parent = map.get(c.getParentId());
            CommentDto top = parent;

            while (top.getParentId() != null) {
                top = map.get(top.getParentId());
            }

            // top 은 최상위 root
            if (top.getReplies() == null)
                top.setReplies(new ArrayList<>());

            top.getReplies().add(c);
        }

        return roots;
    }

    private List<CommentDto> flattenComments(List<CommentDto> dtos) {

        // parentId == null → 상위댓글
        List<CommentDto> roots = dtos.stream()
                .filter(c -> c.getParentId() == null)
                .collect(Collectors.toList());

        // 나머지 → 대댓글 리스트
        List<CommentDto> replies = dtos.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.toList());

        // 상위 댓글별로 자신의 대댓글들을 묶어줌 (UI에서 평면으로 표시)
        for (CommentDto root : roots) {
            List<CommentDto> childList = replies.stream()
                    .filter(r -> r.getParentId().equals(root.getId()))
                    .collect(Collectors.toList());

            root.setReplies(childList); // 깊이 1단계만 유지
        }

        return roots;
    }

    // ---------------- DTO 변환 ----------------
    private CommentDto toDto(CommentEntity c) {

        String nickname = c.getUser().getNickname();
        String email = c.getUser().getEmail();

        // --------------------------
        // ⭐ 캐릭터 프로필 이미지 가져오기
        // --------------------------
        String profileImage = characterService
                .getCharactersByUser(email)
                .stream()
                .filter(ch -> ch.getNickname().equalsIgnoreCase(nickname))
                .findFirst()
                .map(ch -> ch.getCharacterImageUrl())
                .orElse("");

        return CommentDto.builder()
                .id(c.getId())
                .parentId(c.getParent() == null ? null : c.getParent().getId())
                .parentNickname(c.getParent() == null ? null : c.getParent().getUser().getNickname()) // ⭐ 부모 닉네임 설정
                .nickname(nickname)
                .profileImage(profileImage)
                .content(c.isDeleted() ? "(삭제된 댓글입니다.)" : c.getContent())
                .createdAt(c.getCreatedAt().toString())
                .deleted(c.isDeleted())
                .build();
    }

    // 댓글을 "최상위 댓글(c)" 아래에만 모아주는 버전
    private List<CommentDto> buildReplyTree(List<CommentDto> list) {

        // ID → DTO 매핑
        Map<Long, CommentDto> map = list.stream()
                .collect(Collectors.toMap(CommentDto::getId, c -> c));

        List<CommentDto> roots = new ArrayList<>();

        for (CommentDto c : list) {

            // ⭐ 최상위 댓글
            if (c.getParentId() == null) {
                roots.add(c);
                continue;
            }

            // ⭐ parentId를 가진 답글(r) 또는 r2, r3 모두 한 곳에 모음
            CommentDto parent = map.get(c.getParentId());

            // 부모가 최상위 댓글인지 확인
            CommentDto topParent = parent;

            // 부모가 최상위가 아닐 경우, 최상위 부모를 찾는다
            while (topParent.getParentId() != null) {
                topParent = map.get(topParent.getParentId());
            }

            // 최상위 부모(topParent)에 붙인다 (즉, 같은 계층으로 보여줌)
            if (topParent.getReplies() == null) {
                topParent.setReplies(new ArrayList<>());
            }

            topParent.getReplies().add(c);
        }

        return roots;
    }

    private List<CommentEntity> collectAllReplies(Long parentId) {
        List<CommentEntity> all = new ArrayList<>();

        // 1차 대댓글 가져오기
        List<CommentEntity> first = commentRepository.findByParentId(parentId);

        for (CommentEntity reply : first) {
            all.add(reply);
            collectChildren(reply.getId(), all); // 재귀적으로 children 수집
        }

        return all;
    }

    private void collectChildren(Long parentId, List<CommentEntity> list) {
        List<CommentEntity> children = commentRepository.findByParentId(parentId);

        for (CommentEntity child : children) {
            list.add(child);
            collectChildren(child.getId(), list);
        }
    }

}
