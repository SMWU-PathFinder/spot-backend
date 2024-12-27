package com.pathfinder.spot.dto.placeFav;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FavUpdateRequest(
        @NotNull(message = "장소 이름은 필수입니다.")
        @Size(max = 150, message = "장소 이름은 150자 이하이어야 합니다.")
        String placeName,

        @NotNull(message = "카테고리는 필수입니다.")
        @Pattern(
                regexp = "^(식사|휴식|수업|모임|기기 충전|대기|회의|채광|풍경|수다|기타)$",
                message = "카테고리는 '식사', '휴식', '수업', '모임', '기기 충전', '대기', '회의', '채광', '풍경', '수다', '기타' 중 하나여야 합니다."
        )
        String categoryName,
        @Size(max = 100, message = "메모는 100자 이하이어야 합니다.")
        String memo
        ) {
}
