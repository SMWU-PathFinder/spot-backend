package com.pathfinder.spot.api.report;

import com.pathfinder.spot.application.report.ReportService;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.dto.report.ReportRequest;
import com.pathfinder.spot.dto.report.ReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ApiResponse<ReportResponse>> getReport(Authentication authentication) {
        String email = authentication.getName();
        return reportService.getReport(email);
    }
}
