package com.example.loaplan.domain.board.service;

import com.example.loaplan.domain.board.dto.BoardCreateDto;
import com.example.loaplan.domain.board.dto.BoardDetailDto;
import com.example.loaplan.domain.board.dto.BoardListDto;
import com.example.loaplan.domain.board.entity.BoardCategory;
import com.example.loaplan.domain.board.entity.BoardEntity;
import com.example.loaplan.domain.board.entity.FileEntity;
import com.example.loaplan.domain.board.repository.BoardRepository;
import com.example.loaplan.domain.board.repository.FileRepository;
import com.example.loaplan.domain.user.entity.UserEntity;
import com.example.loaplan.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Value("${app.server-url}")
    private String serverUrl;

    public BoardListDto toListDto(BoardEntity entity) {

        List<FileEntity> files = fileRepository.findByBoardIdOrderByUploadedAtAsc(entity.getId());
        List<String> urls = files.stream()
                .map(f -> "/uploads/" + f.getStoredName())
                .toList();

        return BoardListDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .nickname(entity.getUser().getNickname())
                .category(entity.getCategory().name())

                .imageUrls(urls)
                .commentCount(entity.getCommentCount())
                .likeCount(entity.getLikeCount())
                .viewCount(entity.getViewCount())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public BoardEntity create(BoardCreateDto dto, Long userId, String ip) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow();

        BoardCategory category = BoardCategory.valueOf(dto.getCategoryCode());

        BoardEntity board = BoardEntity.builder()
                .user(user)
                .category(category)
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(0)
                .isNotice(false)
                .isDeleted(false)
                .commentCount(0)
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .ipAddress(ip)
                .build();

        return boardRepository.save(board);
    }

    public List<BoardEntity> findByCategory(BoardCategory category) {
        return boardRepository
                .findByCategoryAndIsDeletedFalseOrderByIdDesc(category);
    }


    public BoardDetailDto toDetailDto(BoardEntity entity) {

        List<FileEntity> files = fileRepository.findByBoardIdOrderByUploadedAtAsc(entity.getId());
        List<String> urls = files.stream()
                .map(f -> "/uploads/" + f.getStoredName())
                .toList();

        return BoardDetailDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .nickname(entity.getUser().getNickname())
                .createdAt(entity.getCreatedAt())
                .imageUrls(urls)
                .viewCount(entity.getViewCount())
                .commentCount(entity.getCommentCount())
                .likeCount(entity.getLikeCount())
                .build();
    }

    @Transactional
    public BoardEntity getDetailWithViewIncrease(Long id) {
        // 1) 조회수 +1
        boardRepository.increaseViewCount(id);

        // 2) 엔티티 다시 조회 (증가된 조회수 포함)
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
    }

}
