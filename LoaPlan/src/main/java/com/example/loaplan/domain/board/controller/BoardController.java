package com.example.loaplan.domain.board.controller;

import com.example.loaplan.domain.board.dto.BoardCreateDto;
import com.example.loaplan.domain.board.entity.BoardCategory;
import com.example.loaplan.domain.board.entity.BoardEntity;
import com.example.loaplan.domain.board.entity.FileEntity;
import com.example.loaplan.domain.board.repository.BoardRepository;
import com.example.loaplan.domain.board.service.BoardService;
import com.example.loaplan.domain.board.service.FileService;
import com.example.loaplan.global.springSecurity.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final BoardRepository boardRepository;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            @RequestPart("data") BoardCreateDto dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails user,   // ⭐ 로그인 정보 자동 주입!
            HttpServletRequest request
    ) throws Exception {

        Long userId = user.getId();                   // ⭐ 로그인 유저 ID
        String ip = request.getRemoteAddr();

        BoardEntity board = boardService.create(dto, userId, ip);

        if (files != null && !files.isEmpty()) {
            fileService.upload(board.getId(), userId, files);
        }

        return ResponseEntity.ok(boardService.toListDto(board));
    }



    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam String category) {

        BoardCategory boardCategory = BoardCategory.valueOf(category);
        List<BoardEntity> list = boardService.findByCategory(boardCategory);

        return ResponseEntity.ok(
                list.stream()
                        .map(boardService::toListDto)
                        .toList()
        );
    }


    @GetMapping("/images")
    public ResponseEntity<?> getImages(@RequestParam Long boardId) {

        List<FileEntity> files = fileService.getFiles(boardId);

        List<String> urls = files.stream()
                .map(f -> "/uploads/" + f.getStoredName())
                .toList();

        return ResponseEntity.ok(urls);
    }

    @GetMapping("/detail")
    @Transactional
    public ResponseEntity<?> detail(@RequestParam Long id) {

        // ⭐ 1) 조회수 증가 (UPDATE)
        boardRepository.increaseViewCount(id);

        // ⭐ 2) 최신 값으로 다시 조회 (viewCount 증가된 값)
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow();

        return ResponseEntity.ok(boardService.toDetailDto(board));
    }



}
