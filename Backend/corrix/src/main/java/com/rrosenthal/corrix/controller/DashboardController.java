package com.rrosenthal.corrix.controller;

import com.rrosenthal.corrix.dto.DashboardSummaryResponse;
import com.rrosenthal.corrix.service.CapaService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final CapaService capaService;
    public DashboardController(CapaService capaService) {
        this.capaService = capaService;
    }

    @GetMapping("/api/dashboard/summary")
    public DashboardSummaryResponse getDashboardSummary() {
        return capaService.getDashboardSummary();
    }
}
