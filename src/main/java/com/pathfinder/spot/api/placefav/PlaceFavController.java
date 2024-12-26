package com.pathfinder.spot.api.placefav;

import com.pathfinder.spot.application.placefav.PlaceFavService;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.dto.placeFav.FavRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fav")
public class PlaceFavController {
    private final PlaceFavService placeFavService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addPlaceFav(Authentication authentication, @RequestBody @Valid FavRequest favRequest) {
        String email = authentication.getName();
        return placeFavService.addPlaceFav(email, favRequest);
    }
}
