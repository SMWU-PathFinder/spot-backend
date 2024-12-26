package com.pathfinder.spot.application.placefav;

import com.pathfinder.spot.common.UserInfoUtil;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.domain.category.Category;
import com.pathfinder.spot.domain.category.CategoryRepository;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.placefav.PlaceFav;
import com.pathfinder.spot.dto.placeFav.FavRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PlaceFavService {

    private final UserInfoUtil userInfoUtil;
    private final CategoryRepository categoryRepository;
    @Transactional
    public ResponseEntity<ApiResponse<Void>> addPlaceFav(String email, FavRequest favRequest) {
        String placeName = favRequest.placeName();
        log.info("placeName: {}", placeName);
        Member member = userInfoUtil.getUserInfoByEmail(email);
        log.info("member Id: {}", member.getId());
        Category category = categoryRepository.findByCategoryName("기타")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        log.info("category Id: {}", category.getId());
        PlaceFav.builder()
                .placeName(placeName)
                .member(member)
                .category(category)
                .build();
        return ResponseEntity.ok(ApiResponse.success(null, "즐겨찾기 추가 성공"));
    }
}
