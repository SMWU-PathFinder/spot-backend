package com.pathfinder.spot.dto.placeFav;

public record FavResponse(
        Long placeId,
        String placeName,
        String memo,
        String categoryName) {
    public static FavResponse of(Long placeId, String placeName, String memo, String categoryName) {
        return new FavResponse(
                placeId,
                placeName,
                memo,
                categoryName
        );
    }
}
