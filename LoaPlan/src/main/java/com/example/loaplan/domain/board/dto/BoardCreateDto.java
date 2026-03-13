package com.example.loaplan.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCreateDto {
    private String title;
    private String content;
    private String categoryCode;
    private boolean hideNickname;
}
