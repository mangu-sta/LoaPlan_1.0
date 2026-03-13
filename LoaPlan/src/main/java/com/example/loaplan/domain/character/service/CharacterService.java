package com.example.loaplan.domain.character.service;

import com.example.loaplan.domain.character.dto.CharacterDto;
import com.example.loaplan.domain.character.entity.CharacterEntity;
import com.example.loaplan.domain.character.repository.CharacterRepository;
import com.example.loaplan.domain.user.entity.UserEntity;
import com.example.loaplan.domain.user.repository.UserRepository;
import com.example.loaplan.global.api.LostArkApiClient;
import com.example.loaplan.global.api.dto.CharacterProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final LostArkApiClient loaApiClient;

    @Transactional
    public CharacterDto addCharacter(String nickname, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        CharacterEntity entity = characterRepository
                .findByUserAndNickname(user, nickname)
                .orElse(CharacterEntity.builder()
                        .user(user)
                        .nickname(nickname)
                        .build());

        // ✅ Lost Ark API 호출
        CharacterProfileDto profile = loaApiClient.getProfile(nickname);
        // 간단한 확인용 로그
        System.out.println("===== 🏝️ 캐릭터 api(확인용)=====");
        System.out.println("캐릭터 레벨 :" + profile.getCharacterLevel());
        System.out.println("캐릭텅 이미지 : " + profile.getCharacterImage());
        System.out.println("캐릭터 이름 : " + profile.getCharacterName());
        System.out.println("캐릭터 칭호 : " + profile.getTitle());
        System.out.println("캐릭터 길드 : " + profile.getGuildName());
        System.out.println("캐릭터 서버 : " + profile.getServerName());
        System.out.println("캐릭터 직업 : " + profile.getCharacterClassName());
        System.out.println("캐릭터 아이템레벨 : " + profile.getItemMaxLevel());
        System.out.println("========================================");

        if (profile != null) {
            entity.setServerName(profile.getServerName());
            entity.setClassName(profile.getCharacterClassName());
            entity.setCharacterImageUrl(profile.getCharacterImage());
            try {
                String rawLevel = profile.getItemMaxLevel();
                entity.setItemLevel(
                        (rawLevel != null && !rawLevel.isBlank())
                                ? new BigDecimal(rawLevel.replace(",", ""))
                                : BigDecimal.ZERO);
            } catch (Exception e) {
                System.out.println("⚠️ 아이템 레벨 변환 실패: " + e.getMessage());
                entity.setItemLevel(BigDecimal.ZERO);
            }
        }

        entity.setUpdatedAt(LocalDateTime.now());
        CharacterEntity saved = characterRepository.save(entity);
        return CharacterDto.fromEntity(saved);
    }

    // ✅ 로그인 유저 캐릭터 목록 정렬된 순서로 조회
    @Transactional(readOnly = true)
    public List<CharacterDto> getCharactersByUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        return characterRepository.findByUserOrderByOrderIndexAsc(user)
                .stream()
                .map(CharacterDto::fromEntity)
                .toList();
    }

    // ✅ 캐릭터 순서 업데이트 (드래그 후 저장)
    @Transactional
    public void updateOrder(String email, List<Long> orderedIds) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        for (int i = 0; i < orderedIds.size(); i++) {
            final int order = i;
            Long id = orderedIds.get(i);
            characterRepository.findByIdAndUser(id, user).ifPresent(ch -> {
                ch.setOrderIndex(order);
            });
        }
    }

    public String getProfileImageByNickname(String nickname) {

        UserEntity user = userRepository.findByNickname(nickname)
                .orElse(null);

        if (user == null)
            return null;

        CharacterEntity character = characterRepository
                .findTopByUserIdOrderByIdAsc(user.getId())
                .orElse(null);

        if (character == null)
            return null;

        return character.getCharacterImageUrl();
    }

    // ✅ 캐릭터 삭제
    @Transactional
    public void deleteCharacter(Long characterId, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        CharacterEntity character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터"));

        if (!character.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        characterRepository.delete(character);
    }

    // ✅ 캐릭터 정보 동기화 (새로고침)
    @Transactional
    public List<CharacterDto> refreshCharacters(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        List<CharacterEntity> characters = characterRepository.findByUserOrderByOrderIndexAsc(user);

        for (CharacterEntity entity : characters) {
            try {
                CharacterProfileDto profile = loaApiClient.getProfile(entity.getNickname());

                if (profile != null) {
                    entity.setServerName(profile.getServerName());
                    entity.setClassName(profile.getCharacterClassName());
                    entity.setCharacterImageUrl(profile.getCharacterImage());

                    try {
                        String rawLevel = profile.getItemMaxLevel();
                        entity.setItemLevel(
                                (rawLevel != null && !rawLevel.isBlank())
                                        ? new BigDecimal(rawLevel.replace(",", ""))
                                        : BigDecimal.ZERO);
                    } catch (Exception e) {
                        entity.setItemLevel(BigDecimal.ZERO);
                    }
                    entity.setUpdatedAt(LocalDateTime.now());
                }
            } catch (Exception e) {
                System.out.println("❌ 캐릭터 갱신 실패 (" + entity.getNickname() + "): " + e.getMessage());
                // 개별 실패하더라도 계속 진행
            }
        }

        // 변경된 엔티티들은 트랜잭션 종료 시 자동 저장 (Dirty Checking)
        // DTO 리스트 반환
        return characters.stream().map(CharacterDto::fromEntity).toList();
    }

}
