package com.pathfinder.spot.dto.placeFav;

import com.pathfinder.spot.domain.placefav.PlaceFav;

public record FavPlaceResponse(
        Long placeId,
        String placeName,
        String memo
) {
    public static FavPlaceResponse of(PlaceFav placeFav) {
        return new FavPlaceResponse(
                placeFav.getId(),
                placeFav.getPlaceName(),
                placeFav.getMemo()
        );
    }
}

