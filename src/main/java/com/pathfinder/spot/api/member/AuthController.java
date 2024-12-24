package com.pathfinder.spot.api.member;

import com.pathfinder.spot.application.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/login/{provider}")
    public ResponseEntity<?> kakaoLogin(@PathVariable("provider") final String provider,
                                        @RequestParam("code") final String code) {
        return memberService.login(provider, code);
    }
}
