package com.pathfinder.spot.api.report;

import com.pathfinder.spot.application.report.ReportService;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.dto.placeFav.FavRequest;
import com.pathfinder.spot.dto.report.ReportRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addReport(Authentication authentication, @RequestBody @Valid ReportRequest reportRequest) {
        String email = authentication.getName();
        return reportService.addReport(email, reportRequest);
    }
}
