package com.example.loaplan.global.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterArmoryDto {
    private CharacterProfileDto profile;
    private List<EquipmentDto> equipment;
}

