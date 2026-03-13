package com.example.loaplan.domain.item.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    private String apiCode;
    private String name;
    private String type;
    private String grade;
    private Integer price;
}