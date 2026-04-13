package com.rrosenthal.corrix.controller;

import com.rrosenthal.corrix.dto.AgingReportResponse;
import com.rrosenthal.corrix.service.CapaService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    private final CapaService capaService;

    public ReportController(CapaService capaService) {
        this.capaService = capaService;
    }

    @GetMapping("/api/reports/open-capa-aging")
    public AgingReportResponse getOpenCapaAgingReport(){
        return capaService.getOpenCapaAgingReport();
    }
}
