package com.example.loaplan.domain.board.controller;

import com.example.loaplan.domain.board.dto.CommentDto;
import com.example.loaplan.domain.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentDto dto, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(commentService.createComment(dto, email));
    }

    // 댓글 목록
    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam Long boardId) {
        return ResponseEntity.ok(commentService.getComments(boardId));
    }
}
