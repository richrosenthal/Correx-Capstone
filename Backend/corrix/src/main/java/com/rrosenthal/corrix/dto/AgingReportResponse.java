package com.rrosenthal.corrix.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class AgingReportResponse {

    private String title;
    private OffsetDateTime generatedAt;
    private List<AgingReportRowResponse> rows;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public OffsetDateTime getGeneratedAt() {
        return generatedAt;
    }
    public void setGeneratedAt(OffsetDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
    public List<AgingReportRowResponse> getRows() {
        return rows;
    }
    public void setRows(List<AgingReportRowResponse> rows) {
        this.rows = rows;
    }
}
