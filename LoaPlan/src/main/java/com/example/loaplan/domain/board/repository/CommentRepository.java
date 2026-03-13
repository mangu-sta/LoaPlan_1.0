package com.example.loaplan.domain.board.repository;

import com.example.loaplan.domain.board.entity.BoardEntity;
import com.example.loaplan.domain.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // 삭제되지 않은 댓글만, 오래된 순으로
    List<CommentEntity> findByBoardAndIsDeletedFalseOrderByIdAsc(BoardEntity board);
    List<CommentEntity> findByParentId(Long parentId);

}
