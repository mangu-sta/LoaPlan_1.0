package com.example.loaplan.domain.character.dto;

import com.example.loaplan.domain.character.entity.CharacterEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CharacterDto {
    private Long id;
    private String nickname;
    private String serverName;
    private String className;
    private BigDecimal itemLevel;
    private String characterImageUrl;
    private LocalDateTime updatedAt;

    public static CharacterDto fromEntity(CharacterEntity e) {
        return CharacterDto.builder()
                .id(e.getId())
                .nickname(e.getNickname())
                .serverName(e.getServerName())
                .className(e.getClassName())
                .itemLevel(e.getItemLevel())
                .characterImageUrl(e.getCharacterImageUrl())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
