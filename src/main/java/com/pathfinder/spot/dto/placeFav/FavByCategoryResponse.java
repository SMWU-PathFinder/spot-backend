package com.pathfinder.spot.dto.placeFav;

import java.util.List;

public record FavByCategoryResponse(
        String categoryName,             // 카테고리 이름
        List<FavPlaceResponse> favPlaces // 해당 카테고리의 즐겨찾기 장소 리스트
) {}