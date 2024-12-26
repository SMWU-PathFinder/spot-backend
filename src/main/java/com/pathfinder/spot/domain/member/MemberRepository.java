package com.pathfinder.spot.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialLoginId(final String socialLoginId);

    Optional<Member> findByEmail(final String email);
}
