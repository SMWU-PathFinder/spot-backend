package com.pathfinder.spot.dto.report;

import java.util.List;

public record ReportResponse(
        List<ReportList> reportList
) {
}
