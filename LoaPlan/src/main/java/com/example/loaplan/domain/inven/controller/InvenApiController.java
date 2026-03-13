package com.example.loaplan.domain.inven.controller;

import com.example.loaplan.domain.inven.dto.PostDTO;
import com.example.loaplan.domain.inven.service.InvenCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inven")
public class InvenApiController {

    private final InvenCrawlerService invenCrawlerService;

    @GetMapping("/search")
    public List<PostDTO> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") int page
    ) throws IOException {

        if (q == null || q.isBlank()) {
            return List.of();
        }

        return invenCrawlerService.searchPosts(q, page);
    }
}
