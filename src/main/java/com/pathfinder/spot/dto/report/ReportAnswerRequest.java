package com.pathfinder.spot.dto.report;

import jakarta.validation.constraints.NotNull;

public record ReportAnswerRequest(
        @NotNull(message = "신고글 답변 내용은 필수입니다.")
        String answer
) {
}
