package com.pathfinder.spot.dto.report;

import com.pathfinder.spot.domain.report.Report;
import java.time.LocalDateTime;

public record ReportList(
        Long reportId,
        LocalDateTime reportTime,
        String reportTitle,
        String reportContent,
        Boolean reportStatus, // 관리자 답변 유무
        String reportAnswer
) {
    public static ReportList of(Report report, Boolean reportStatus) {
        return new ReportList(
                report.getId(),
                report.getCreatedAt(),
                report.getReportTitle(),
                report.getReportDesc(),
                reportStatus,
                report.getReportAnswer()
        );
    }
}
