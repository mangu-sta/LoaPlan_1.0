package com.example.loaplan.global.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TendencyDto {

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Point")
    private int point;

    @JsonProperty("MaxPoint")
    private int maxPoint;
}
