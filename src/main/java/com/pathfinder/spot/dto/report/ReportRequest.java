package com.pathfinder.spot.dto.report;

import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.report.Report;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportRequest(
        @NotNull(message = "신고글 제목은 필수입니다.")
        @Size(max = 100, message = "신고글 제목은 100자 이하이어야 합니다.")
        String reportTitle,

        @NotNull(message = "신고글 내용은 필수입니다.")
        String reportDesc
) {
    public Report toEntity(Member member) {
        return Report.createReport(reportTitle, reportDesc, member);
    }
}
