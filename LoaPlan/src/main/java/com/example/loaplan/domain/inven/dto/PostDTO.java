package com.example.loaplan.domain.inven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String title;
    private String link;
    private String writer;
    private String date;
}
