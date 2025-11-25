package com.mcabrera.SuperMarket.controllers;


import com.mcabrera.SuperMarket.dtos.TopProductDTO;
import com.mcabrera.SuperMarket.services.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportsController {

    private final ReportService reportService;

    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/branch-sales")
    public ResponseEntity<Map<String, Object>> getBranchSalesReport(
            @RequestParam("branch_id") Long branchId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(reportService.getBranchSalesReport(branchId, start, end));
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDTO>> getTopProducts(
            @RequestParam(value = "limit", defaultValue = "5") int limit) {

        return ResponseEntity.ok(reportService.getTopProducts(limit));
    }
}