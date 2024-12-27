package com.pathfinder.spot.domain.placefav;

import com.pathfinder.spot.common.entity.BaseEntity;
import com.pathfinder.spot.domain.category.Category;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.dto.placeFav.FavUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicInsert
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE place_fav SET status = 'DELETED' where id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class PlaceFav extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String placeName;

    @Column(length = 100)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void updateFavs(FavUpdateRequest favRequest, Category category) {
        this.placeName = favRequest.placeName();
        this.memo = favRequest.memo();
        this.category = category;
    }
}
