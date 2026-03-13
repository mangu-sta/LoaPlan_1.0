package com.example.loaplan.domain.inven.service;

import com.example.loaplan.domain.inven.dto.PostDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvenCrawlerService {

    public List<PostDTO> searchPosts(String keyword, int page) throws IOException {
        List<PostDTO> result = new ArrayList<>();

        // ✅ 검색어 인코딩
        String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        // ✅ 인벤 '검색 결과 페이지' URL
        //    초기 버전과 동일하되 p=부분만 변수로
        String url = "https://www.inven.co.kr/board/lostark/5355" +
                "?query=list&p=" + page +
                "&sterm=&name=subjectcontent&keyword=" + encoded;

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141 Safari/537.36")
                .timeout(10000)
                .get();

        // ✅ 초기 버전과 동일한 셀렉터
        Elements rows = doc.select("div.board-list table tbody tr");

        for (Element row : rows) {
            Element titleEl = row.selectFirst("td.tit a");
            if (titleEl == null) continue;

            String title = titleEl.text().trim();
            String href = titleEl.absUrl("href");

            String writer = row.selectFirst("td.user") != null
                    ? row.selectFirst("td.user").text().trim()
                    : "-";

            String date = row.selectFirst("td.date") != null
                    ? row.selectFirst("td.date").text().trim()
                    : "-";

            // ❗ 여기서 keyword로 필터 안 함! (인벤이 이미 필터함)
            result.add(new PostDTO(title, href, writer, date));
        }

        System.out.println("✅ Inven search - keyword='" + keyword +
                "', page=" + page + ", result size=" + result.size());
        return result;
    }
}
