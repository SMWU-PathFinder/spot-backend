package com.pathfinder.spot.api.report;

import com.pathfinder.spot.application.report.ReportService;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.member.MemberType;
import com.pathfinder.spot.dto.report.ReportAnswerRequest;
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
        Member member = (Member) authentication.getPrincipal();
        boolean isAdmin = member.getRole() == MemberType.ROLE_ADMIN;
        return reportService.getReport(member, isAdmin);
    }

    @PostMapping("/{reportId}/answer")
    public ResponseEntity<ApiResponse<Void>> answerReport(Authentication authentication, @PathVariable Long reportId, @RequestBody @Valid ReportAnswerRequest reportAnswerRequest) {
        Member member = (Member) authentication.getPrincipal();
        boolean isAdmin = member.getRole() == MemberType.ROLE_ADMIN;
        return reportService.answerReport(member, isAdmin, reportId, reportAnswerRequest);
    }
}
