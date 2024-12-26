package com.pathfinder.spot.dto.placeFav;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FavRequest(
        @NotNull(message = "장소 이름은 필수입니다.")
        @Size(max = 150, message = "장소 이름은 150자 이하이어야 합니다.")
        String placeName
) {}
