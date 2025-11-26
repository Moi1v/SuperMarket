package com.mcabrera.SuperMarket.controllers;

import com.mcabrera.SuperMarket.dtos.TopProductDTO;
import com.mcabrera.SuperMarket.services.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getBranchSalesReport(
            @RequestParam(value = "branch_id", required = false) Long branchId,
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {


        if (branchId == null || start == null || end == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Parámetros faltantes",
                            "message", "Este endpoint requiere los siguientes parámetros:",
                            "parametros_requeridos", Map.of(
                                    "branch_id", "ID de la sucursal (número)",
                                    "start", "Fecha inicial en formato ISO (yyyy-MM-ddTHH:mm:ss)",
                                    "end", "Fecha final en formato ISO (yyyy-MM-ddTHH:mm:ss)"
                            ),
                            "ejemplo", "/api/v1/reports/branch-sales?branch_id=1&start=2024-11-01T00:00:00&end=2024-11-30T23:59:59",
                            "ejemplos_adicionales", List.of(
                                    "Sucursal 1 - Noviembre 2024: /api/v1/reports/branch-sales?branch_id=1&start=2024-11-01T00:00:00&end=2024-11-30T23:59:59",
                                    "Sucursal 2 - Hoy: /api/v1/reports/branch-sales?branch_id=2&start=2024-11-26T00:00:00&end=2024-11-26T23:59:59"
                            )
                    ));
        }

        return ResponseEntity.ok(reportService.getBranchSalesReport(branchId, start, end));
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDTO>> getTopProducts(
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit) {

        return ResponseEntity.ok(reportService.getTopProducts(limit));
    }
}
