package com.pathfinder.spot.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum MemberType implements GrantedAuthority {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    public final String type;

    @Override
    public String getAuthority() {
        return type;
    }

    // System.out.println(role.name());         // "ROLE_USER" (Enum 이름)
    //System.out.println(role.getType());      // "user" (type 필드 값)
    //System.out.println(role.getAuthority()); // "user" (Spring Security 권한 값)
}
