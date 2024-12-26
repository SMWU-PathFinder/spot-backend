package com.pathfinder.spot.common;

import com.pathfinder.spot.common.constants.ExceptionCode;
import com.pathfinder.spot.common.exceptions.InvalidInputException;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserInfoUtil {

    private final MemberRepository memberRepository;

    public Member getUserInfoByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidInputException(ExceptionCode.INVALID_USER));
    }
}