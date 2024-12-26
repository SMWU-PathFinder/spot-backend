package com.pathfinder.spot.application.placefav;

import com.pathfinder.spot.common.UserInfoUtil;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.domain.category.Category;
import com.pathfinder.spot.domain.category.CategoryRepository;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.placefav.PlaceFav;
import com.pathfinder.spot.domain.placefav.PlaceFavRepository;
import com.pathfinder.spot.dto.placeFav.FavRequest;
import com.pathfinder.spot.dto.placeFav.FavResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PlaceFavService {

    private final UserInfoUtil userInfoUtil;
    private final CategoryRepository categoryRepository;
    private final PlaceFavRepository placeFavRepository;

    @Transactional
    public ResponseEntity<ApiResponse<Void>> addPlaceFav(String email, FavRequest favRequest) {
        String placeName = favRequest.placeName();
        log.info("placeName: {}", placeName);
        Member member = userInfoUtil.getUserInfoByEmail(email);
        log.info("member Id: {}", member.getId());
        Category category = categoryRepository.findByCategoryName("기타")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        log.info("category Id: {}", category.getId());
        PlaceFav placeFav = PlaceFav.builder()
                .placeName(placeName)
                .member(member)
                .category(category)
                .build();
        placeFavRepository.save(placeFav);
        return ResponseEntity.ok(ApiResponse.success(null, "즐겨찾기 추가 성공"));
    }

    public ResponseEntity<ApiResponse<List<FavResponse>>> getPlaceFav(String email) {
        Member member = userInfoUtil.getUserInfoByEmail(email);
        List<PlaceFav> placeFavList = placeFavRepository.findByMember(member);

        List<FavResponse> favResponses = placeFavList.stream()
                .map(placeFav -> FavResponse.of(
                        placeFav.getId(),
                        placeFav.getPlaceName(),
                        placeFav.getMemo(),
                        placeFav.getCategory().getCategoryName()
                ))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(favResponses, "즐겨찾기 조회 성공"));
    }
}
