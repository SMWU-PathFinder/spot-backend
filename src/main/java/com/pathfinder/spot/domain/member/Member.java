package com.pathfinder.spot.domain.member;

import com.pathfinder.spot.common.entity.BaseEntity;
import com.pathfinder.spot.domain.placefav.PlaceFav;
import com.pathfinder.spot.domain.report.Report;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE member SET status = 'DELETED' where id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 100)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private SocialLoginType socialLoginType;

    @Column(nullable = false)
    private String socialLoginId;

    @OneToMany(mappedBy = "member")
    private List<Report> reports;

    @OneToMany(mappedBy = "member")
    private List<PlaceFav> placeFavs;
}
