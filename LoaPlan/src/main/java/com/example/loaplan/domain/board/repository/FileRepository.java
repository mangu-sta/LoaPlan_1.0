package com.example.loaplan.domain.board.repository;

import com.example.loaplan.domain.board.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByBoardIdOrderByUploadedAtAsc(Long boardId);
}