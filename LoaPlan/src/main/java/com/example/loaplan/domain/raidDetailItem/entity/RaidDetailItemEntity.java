package com.example.loaplan.domain.raidDetailItem.entity;
import com.example.loaplan.domain.item.entity.ItemEntity;
import com.example.loaplan.domain.raidDetail.entity.RaidDetailEntity;
import jakarta.persistence.*; import lombok.*;

@Entity
@Table(name = "raid_detail_item")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RaidDetailItemEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raid_detail_id", nullable = false)
    private RaidDetailEntity raidDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    private Integer quantity = 1;
}