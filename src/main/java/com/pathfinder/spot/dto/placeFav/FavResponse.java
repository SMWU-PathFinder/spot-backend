package com.pathfinder.spot.dto.placeFav;

import java.util.List;

public record FavResponse(
        List<FavByCategoryResponse> favPlaceByCategories // 카테고리별 장소 리스트
) {
}
