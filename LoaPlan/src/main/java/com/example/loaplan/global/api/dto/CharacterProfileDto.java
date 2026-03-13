package com.example.loaplan.global.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class CharacterProfileDto {

    @JsonProperty("CharacterName")
    private String characterName;

    @JsonProperty("ServerName")
    private String serverName;

    @JsonProperty("CharacterClassName")
    private String characterClassName;

    @JsonProperty("ItemAvgLevel")
    private String itemMaxLevel;

    @JsonProperty("CharacterLevel")
    private int characterLevel;

    @JsonProperty("GuildName")
    private String guildName;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("CharacterImage")
    private String characterImage;

    // ⭐ 추가해야 모달 완성되는 필드
    @JsonProperty("ExpeditionLevel")
    private int expeditionLevel;

    @JsonProperty("CombatPower")
    private String combatPower;

    // Stats 배열
    @JsonProperty("Stats")
    private List<ProfileStatDto> stats;

    // 성향 (지성/담력/매력/친절)
    @JsonProperty("Tendencies")
    private List<TendencyDto> tendencies;
}

