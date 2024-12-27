package com.pathfinder.spot.domain.report;

import com.pathfinder.spot.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByMemberOrderByUpdatedAtDesc(Member member);
}
