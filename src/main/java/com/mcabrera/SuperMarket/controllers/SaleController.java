package com.mcabrera.SuperMarket.controllers;

import com.mcabrera.SuperMarket.dtos.SaleRequestDTO;
import com.mcabrera.SuperMarket.dtos.SaleResponseDTO;
import com.mcabrera.SuperMarket.services.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody SaleRequestDTO saleRequestDTO) {
        SaleResponseDTO created = saleService.save(saleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAllSales() {
        return ResponseEntity.ok(saleService.getAll());
    }

    @GetMapping("/{sale_id}")
    public ResponseEntity<SaleResponseDTO> getSaleById(@PathVariable("sale_id") Long saleId) {
        return ResponseEntity.ok(saleService.getById(saleId));
    }

    @DeleteMapping("/{sale_id}")
    public ResponseEntity<String> deleteSale(@PathVariable("sale_id") Long saleId) {
        saleService.deleteById(saleId);
        return ResponseEntity.ok("Venta eliminada exitosamente y stock restaurado");
    }
}