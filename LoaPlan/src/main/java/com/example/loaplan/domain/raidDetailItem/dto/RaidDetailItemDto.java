package com.example.loaplan.domain.raidDetailItem.dto;
import lombok.*;



@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RaidDetailItemDto {
    private Long id;
    private Long raidDetailId;
    private Long itemId;
    private Integer quantity;
}