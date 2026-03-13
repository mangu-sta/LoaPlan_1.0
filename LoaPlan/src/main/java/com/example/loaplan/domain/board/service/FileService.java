package com.example.loaplan.domain.board.service;

import com.example.loaplan.domain.board.entity.BoardEntity;
import com.example.loaplan.domain.board.entity.FileEntity;
import com.example.loaplan.domain.board.repository.BoardRepository;
import com.example.loaplan.domain.board.repository.FileRepository;
import com.example.loaplan.domain.user.entity.UserEntity;
import com.example.loaplan.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${app.upload.dir}")
    private String uploadDir; // 예: "uploads"

    private final FileRepository fileRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private Path rootUploadPath;

    @PostConstruct
    public void init() {
        // 🟡 프로젝트 루트 경로 가져오기
        String projectRoot = System.getProperty("user.dir");

        // 🟡 uploads 폴더 절대경로 생성
        rootUploadPath = Paths.get(projectRoot, uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(rootUploadPath);
            System.out.println("📁 Upload directory initialized: " + rootUploadPath);
        } catch (IOException e) {
            throw new RuntimeException("업로드 폴더 생성 실패: " + rootUploadPath, e);
        }
    }

    public void upload(Long boardId, Long userId, List<MultipartFile> files) throws IOException {

        BoardEntity board = boardRepository.findById(boardId).orElseThrow();
        UserEntity user = userRepository.findById(userId).orElseThrow();

        boolean thumbnailSet = false;

        for (MultipartFile mf : files) {

            String origin = mf.getOriginalFilename();
            String ext = origin.substring(origin.lastIndexOf("."));
            String stored = UUID.randomUUID() + ext;

            // 🟡 저장 절대경로
            Path savePath = rootUploadPath.resolve(stored);

            // 파일 저장
            Files.copy(mf.getInputStream(), savePath);

            // DB 저장
            FileEntity entity = FileEntity.builder()
                    .board(board)
                    .user(user)
                    .originalName(origin)
                    .storedName(stored)
                    .filePath(savePath.toString())   // 서버 물리 경로
                    .fileType(mf.getContentType())
                    .fileSize(mf.getSize())
                    .isActive(true)
                    .isThumbnail(false)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            fileRepository.save(entity);

            // 썸네일 지정
            if (!thumbnailSet) {
                thumbnailSet = true;
                boardRepository.save(board);
            }
        }
    }


    public List<FileEntity> getFiles(Long boardId) {
        return fileRepository.findByBoardIdOrderByUploadedAtAsc(boardId);
    }

}
