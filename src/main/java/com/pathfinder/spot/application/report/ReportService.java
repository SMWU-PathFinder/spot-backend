package com.pathfinder.spot.application.report;

import com.pathfinder.spot.common.UserInfoUtil;
import com.pathfinder.spot.common.constants.ExceptionCode;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.common.exceptions.BadRequestException;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.report.Report;
import com.pathfinder.spot.domain.report.ReportRepository;
import com.pathfinder.spot.dto.report.ReportAnswerRequest;
import com.pathfinder.spot.dto.report.ReportList;
import com.pathfinder.spot.dto.report.ReportRequest;
import com.pathfinder.spot.dto.report.ReportResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final UserInfoUtil userInfoUtil;
    private final ReportRepository reportRepository;

    @Transactional
    public ResponseEntity<ApiResponse<Void>> addReport(String email, ReportRequest reportRequest) {
        Member member = userInfoUtil.getUserInfoByEmail(email);
        reportRepository.save(reportRequest.toEntity(member));
        return ResponseEntity.ok(ApiResponse.success(null, "신고글 작성 성공"));
    }

    public ResponseEntity<ApiResponse<ReportResponse>> getReport(Member member, Boolean isAdmin) {
        List<Report> reports;
        if (isAdmin) {
            reports = reportRepository.findAllByOrderByUpdatedAtDesc();
        } else {
            reports = reportRepository.findByMemberOrderByUpdatedAtDesc(member);
        }
        List<ReportList> reportLists = reports.stream()
                .map(report -> ReportList.of(report, report.getReportAnswer() != null))
                .peek(reportList -> log.info("reportList: {}", reportList))
                .toList();
        ReportResponse reportResponse = new ReportResponse(reportLists);
        return ResponseEntity.ok(ApiResponse.success(reportResponse, "신고글 조회 성공"));
    }

    @Transactional
    public ResponseEntity<ApiResponse<Void>> answerReport(Member member, Boolean isAdmin, Long reportId, ReportAnswerRequest reportAnswerRequest) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_REPORT));
        if (!isAdmin) {
            throw new BadRequestException(ExceptionCode.INVALID_MEMBER);
        }
        report.completeAnswer(reportAnswerRequest.answer());
        return ResponseEntity.ok(ApiResponse.success(null, "관리자 신고글 답변 작성 성공"));
    }
}
