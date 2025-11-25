package com.mcabrera.SuperMarket.services;

import com.mcabrera.SuperMarket.dtos.BranchDTO;
import com.mcabrera.SuperMarket.entities.Branch;
import com.mcabrera.SuperMarket.exceptions.BusinessRuleException;
import com.mcabrera.SuperMarket.exceptions.DuplicateResourceException;
import com.mcabrera.SuperMarket.exceptions.ResourceNotFoundException;
import com.mcabrera.SuperMarket.repositories.IBranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {

    private final IBranchRepository branchRepository;

    public BranchService(IBranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Transactional(readOnly = true)
    public List<BranchDTO> getAll() {
        return branchRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BranchDTO getById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + id));
        return convertToDTO(branch);
    }

    @Transactional
    public BranchDTO save(BranchDTO branchDTO) {
        if (branchRepository.existsByBranchCode(branchDTO.getBranchCode())) {
            throw new DuplicateResourceException("Ya existe una sucursal con el código: " + branchDTO.getBranchCode());
        }

        Branch branch = convertToEntity(branchDTO);
        Branch savedBranch = branchRepository.save(branch);
        return convertToDTO(savedBranch);
    }

    @Transactional
    public BranchDTO update(Long id, BranchDTO branchDTO) {
        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + id));

        if (!existingBranch.getBranchCode().equals(branchDTO.getBranchCode()) &&
                branchRepository.existsByBranchCode(branchDTO.getBranchCode())) {
            throw new DuplicateResourceException("Ya existe una sucursal con el código: " + branchDTO.getBranchCode());
        }

        existingBranch.setName(branchDTO.getName());
        existingBranch.setAddress(branchDTO.getAddress());
        existingBranch.setPhone(branchDTO.getPhone());
        existingBranch.setBranchCode(branchDTO.getBranchCode());

        Branch updatedBranch = branchRepository.save(existingBranch);
        return convertToDTO(updatedBranch);
    }

    @Transactional
    public void deleteById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + id));

        if (!branch.getSales().isEmpty()) {
            throw new BusinessRuleException("No se puede eliminar la sucursal porque tiene ventas registradas");
        }

        branchRepository.deleteById(id);
    }

    private BranchDTO convertToDTO(Branch branch) {
        return new BranchDTO(
                branch.getBranchId(),
                branch.getName(),
                branch.getAddress(),
                branch.getPhone(),
                branch.getBranchCode()
        );
    }

    private Branch convertToEntity(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setBranchId(dto.getBranchId());
        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());
        branch.setPhone(dto.getPhone());
        branch.setBranchCode(dto.getBranchCode());
        return branch;
    }
}