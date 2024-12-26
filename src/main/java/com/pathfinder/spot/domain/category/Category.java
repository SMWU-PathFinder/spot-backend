package com.pathfinder.spot.domain.category;

import com.pathfinder.spot.common.entity.BaseEntity;
import com.pathfinder.spot.domain.placefav.PlaceFav;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE category SET status = 'DELETED' where id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_category_id")
//    private Category parentCategory;
//
//    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Category> subCategories = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<PlaceFav> placeFavs;
}
