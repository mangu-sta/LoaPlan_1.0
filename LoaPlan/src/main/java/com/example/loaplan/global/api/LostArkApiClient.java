package com.example.loaplan.global.api;

import com.example.loaplan.global.api.dto.CharacterArmoryDto;
import com.example.loaplan.global.api.dto.EquipmentDto;
import com.example.loaplan.global.api.dto.CharacterProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LostArkApiClient {

    @Value("${lostark.api.key}")
    private String apiKey;

    public CharacterProfileDto getProfile(String nickname) {
        try {
            String encoded = URLEncoder.encode(nickname, StandardCharsets.UTF_8);
            String url = "https://developer-lostark.game.onstove.com/armories/characters/" + encoded + "/profiles";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "bearer " + apiKey.trim());
            headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<String> resp = new RestTemplate().exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class
            );

            if (resp.getStatusCode().is2xxSuccessful()) {
                ObjectMapper om = new ObjectMapper();
                return om.readValue(resp.getBody(), CharacterProfileDto.class);
            }
        } catch (Exception e) {
            System.out.println("⚠️ LostArk API 호출 실패: " + e.getMessage());
        }
        return null;
    }

    public CharacterArmoryDto getArmory(String nickname) {
        try {
            String encoded = URLEncoder.encode(nickname, StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "bearer " + apiKey.trim());
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            RestTemplate rt = new RestTemplate();

            // 프로필
            String pUrl = "https://developer-lostark.game.onstove.com/armories/characters/" + encoded + "/profiles";
            ResponseEntity<String> profileResp = rt.exchange(
                    pUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class
            );

            // 장비
            String eUrl = "https://developer-lostark.game.onstove.com/armories/characters/" + encoded + "/equipment";
            ResponseEntity<String> equipResp = rt.exchange(
                    eUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class
            );

            ObjectMapper om = new ObjectMapper();
            CharacterProfileDto profile = om.readValue(profileResp.getBody(), CharacterProfileDto.class);
            List<EquipmentDto> equipment = om.readValue(
                    equipResp.getBody(),
                    om.getTypeFactory().constructCollectionType(List.class, EquipmentDto.class)
            );

            return new CharacterArmoryDto(profile, equipment);

        } catch (Exception e) {
            System.out.println("⚠ API 조회 실패: " + e.getMessage());
            return null;
        }
    }



}
