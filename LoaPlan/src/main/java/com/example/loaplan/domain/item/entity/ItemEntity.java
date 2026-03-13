package com.example.loaplan.domain.item.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiCode;
    private String name;
    private String type;
    private String grade;
    private Integer price;
}