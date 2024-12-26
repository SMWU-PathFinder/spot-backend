package com.pathfinder.spot.domain.placefav;

import com.pathfinder.spot.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceFavRepository extends JpaRepository<PlaceFav, Long> {
    List<PlaceFav> findByMember(Member member);
}
