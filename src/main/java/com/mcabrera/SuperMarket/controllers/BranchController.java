package com.mcabrera.SuperMarket.controllers;

import com.mcabrera.SuperMarket.dtos.BranchDTO;
import com.mcabrera.SuperMarket.services.BranchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(@RequestBody BranchDTO branchDTO) {
        BranchDTO created = branchService.save(branchDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAll());
    }

    @GetMapping("/{branch_id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable("branch_id") Long branchId) {
        return ResponseEntity.ok(branchService.getById(branchId));
    }

    @PutMapping("/{branch_id}")
    public ResponseEntity<BranchDTO> updateBranch(
            @PathVariable("branch_id") Long branchId,
            @RequestBody BranchDTO branchDTO) {
        return ResponseEntity.ok(branchService.update(branchId, branchDTO));
    }

    @DeleteMapping("/{branch_id}")
    public ResponseEntity<String> deleteBranch(@PathVariable("branch_id") Long branchId) {
        branchService.deleteById(branchId);
        return ResponseEntity.ok("Sucursal eliminada exitosamente");
    }
}