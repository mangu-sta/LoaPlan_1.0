package com.example.loaplan.domain.island.service;

import com.example.loaplan.domain.island.dto.IslandDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IslandService {

    @Value("${lostark.api.key}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();


    public List<IslandDto> getTodayAdventureIslands() {
        System.out.println("✅ getTodayAdventureIslands() 호출됨");
        final String url = "https://developer-lostark.game.onstove.com/gamecontents/calendar";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("authorization", "bearer " + (apiKey == null ? "" : apiKey.replaceAll("\\s+", "")));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) return List.of();

            JsonNode root = mapper.readTree(resp.getBody());
            ZoneId KST = ZoneId.of("Asia/Seoul");
            LocalDate today = LocalDate.now(KST);
            LocalDateTime now = LocalDateTime.now(KST);

            List<IslandDto> result = new ArrayList<>();

            for (JsonNode node : root) {
                if (!"모험 섬".equals(node.path("CategoryName").asText())) continue;

                // ✅ 등장 시간
                List<String> times = new ArrayList<>();
                for (JsonNode t : node.path("StartTimes")) times.add(t.asText());

                List<String> todayTimes = times.stream()
                        .filter(s -> s.startsWith(today.toString()))
                        .distinct().sorted().collect(Collectors.toList());
                if (todayTimes.isEmpty()) continue;

                // ✅ 다음 등장 시간
                String nextTime = null;
                for (String s : todayTimes) {
                    LocalDateTime slot = LocalDateTime.parse(s);
                    if (!slot.isBefore(now)) { nextTime = s; break; }
                }
                if (nextTime == null) continue;

                // ✅ 보상 이름 리스트 추출
                List<String> rewards = new ArrayList<>();
                JsonNode rewardItems = node.path("RewardItems");
                if (rewardItems.isArray()) {
                    for (JsonNode reward : rewardItems) {
                        JsonNode items = reward.path("Items");
                        if (!items.isArray()) continue;
                        for (JsonNode item : items) {
                            String itemName = item.path("Name").asText("");
                            if (!itemName.isEmpty() && !rewards.contains(itemName)) {
                                rewards.add(itemName);
                            }
                        }
                    }
                }

                result.add(new IslandDto(
                        node.path("ContentsName").asText(""),
                        node.path("Location").asText(""),
                        nextTime,
                        todayTimes,
                        false,
                        rewards
                ));

                // 간단한 확인용 로그
                System.out.println("===== 🏝️ Adventure Island Detected =====");
                System.out.println("이름: " + node.path("ContentsName").asText(""));
                System.out.println("위치: " + node.path("Location").asText(""));
                System.out.println("다음 등장: " + nextTime);
                System.out.println("오늘 전체 시간대: " + todayTimes);
                System.out.println("보상 목록: " + rewards);
                System.out.println("========================================");
            }

            result.sort(Comparator.comparing(IslandDto::getNextTime));
            
            return result.size() > 3 ? result.subList(0, 3) : result;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }


    }


}