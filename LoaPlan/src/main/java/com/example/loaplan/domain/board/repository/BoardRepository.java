package com.example.loaplan.domain.board.repository;

import com.example.loaplan.domain.board.entity.BoardEntity;
import com.example.loaplan.domain.board.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    List<BoardEntity> findByCategoryAndIsDeletedFalseOrderByIdDesc(BoardCategory category);

    @Modifying
    @Query("UPDATE BoardEntity b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void increaseViewCount(@Param("id") Long id);

}
