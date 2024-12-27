package com.pathfinder.spot.domain.report;

import com.pathfinder.spot.common.entity.BaseEntity;
import com.pathfinder.spot.domain.member.Member;
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
@SQLDelete(sql = "UPDATE report SET status = 'DELETED' where id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String reportTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reportDesc;

    @Column(columnDefinition = "TEXT")
    private String reportAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Report createReport(String title, String desc, Member member) {
        return Report.builder()
                .reportTitle(title)
                .reportDesc(desc)
                .member(member)
                .build();
    }
}
