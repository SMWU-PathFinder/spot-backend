package com.pathfinder.spot.application.placefav;

import com.pathfinder.spot.common.UserInfoUtil;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.domain.category.Category;
import com.pathfinder.spot.domain.category.CategoryRepository;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.placefav.PlaceFav;
import com.pathfinder.spot.domain.placefav.PlaceFavRepository;
import com.pathfinder.spot.dto.placeFav.FavPlaceResponse;
import com.pathfinder.spot.dto.placeFav.FavRequest;
import com.pathfinder.spot.dto.placeFav.FavByCategoryResponse;
import com.pathfinder.spot.dto.placeFav.FavResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ResponseEntity<ApiResponse<FavResponse>> getPlaceFav(String email) {
        Member member = userInfoUtil.getUserInfoByEmail(email);
        List<PlaceFav> placeFavList = placeFavRepository.findByMember(member);

        // 카테고리별로 그룹화
        Map<String, List<PlaceFav>> groupedByCategory = placeFavList.stream()
                .collect(Collectors.groupingBy(placeFav -> placeFav.getCategory().getCategoryName()));

        // DTO 변환
        List<FavByCategoryResponse> categoryGroups = groupedByCategory.entrySet().stream()
                .map(entry -> new FavByCategoryResponse(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(FavPlaceResponse::of)
                                .toList()
                ))
                .toList();

        FavResponse response = new FavResponse(categoryGroups);
        return ResponseEntity.ok(ApiResponse.success(response, "카테고리별 즐겨찾기 조회 성공"));
    }
}
