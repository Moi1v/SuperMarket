package com.mcabrera.SuperMarket.services;

import com.mcabrera.SuperMarket.dtos.*;
import com.mcabrera.SuperMarket.entities.Branch;
import com.mcabrera.SuperMarket.entities.Product;
import com.mcabrera.SuperMarket.entities.Sale;
import com.mcabrera.SuperMarket.entities.SaleDetail;
import com.mcabrera.SuperMarket.exceptions.BusinessRuleException;
import com.mcabrera.SuperMarket.exceptions.ResourceNotFoundException;
import com.mcabrera.SuperMarket.repositories.IBranchRepository;
import com.mcabrera.SuperMarket.repositories.IProductRepository;
import com.mcabrera.SuperMarket.repositories.ISaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final ISaleRepository saleRepository;
    private final IBranchRepository branchRepository;
    private final IProductRepository productRepository;

    public SaleService(ISaleRepository saleRepository, IBranchRepository branchRepository,
                       IProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<SaleResponseDTO> getAll() {
        return saleRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SaleResponseDTO getById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));
        return convertToResponseDTO(sale);
    }

    @Transactional
    public SaleResponseDTO save(SaleRequestDTO saleRequestDTO) {
        Branch branch = branchRepository.findById(saleRequestDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + saleRequestDTO.getBranchId()));

        if (saleRequestDTO.getSaleDetails() == null || saleRequestDTO.getSaleDetails().isEmpty()) {
            throw new BusinessRuleException("La venta debe tener al menos un detalle");
        }

        Sale sale = new Sale();
        sale.setBranch(branch);
        sale.setCustomerName(saleRequestDTO.getCustomerName());
        sale.setPaymentMethod(saleRequestDTO.getPaymentMethod());
        sale.setSaleDate(LocalDateTime.now());

        List<SaleDetail> saleDetails = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (SaleDetailRequestDTO detailDTO : saleRequestDTO.getSaleDetails()) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + detailDTO.getProductId()));

            if (product.getStock() < detailDTO.getQuantity()) {
                throw new BusinessRuleException("Stock insuficiente para el producto: " + product.getName() +
                        ". Disponible: " + product.getStock() + ", Solicitado: " + detailDTO.getQuantity());
            }

            if (detailDTO.getQuantity() <= 0) {
                throw new BusinessRuleException("La cantidad debe ser mayor a 0");
            }

            product.setStock(product.getStock() - detailDTO.getQuantity());
            productRepository.save(product);

            SaleDetail saleDetail = new SaleDetail();
            saleDetail.setSale(sale);
            saleDetail.setProduct(product);
            saleDetail.setQuantity(detailDTO.getQuantity());
            saleDetail.setUnitPrice(product.getPrice());
            saleDetail.calculateSubtotal();

            saleDetails.add(saleDetail);
            total = total.add(saleDetail.getSubtotal());
        }

        sale.setSaleDetails(saleDetails);
        sale.setTotal(total);

        Sale savedSale = saleRepository.save(sale);
        return convertToResponseDTO(savedSale);
    }

    @Transactional
    public void deleteById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));

        for (SaleDetail detail : sale.getSaleDetails()) {
            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            productRepository.save(product);
        }

        saleRepository.deleteById(id);
    }

    private SaleResponseDTO convertToResponseDTO(Sale sale) {
        List<SaleDetailResponseDTO> detailDTOs = sale.getSaleDetails().stream()
                .map(detail -> new SaleDetailResponseDTO(
                        detail.getSaleDetailId(),
                        detail.getProduct().getProductId(),
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getUnitPrice(),
                        detail.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new SaleResponseDTO(
                sale.getSaleId(),
                sale.getBranch().getBranchId(),
                sale.getBranch().getName(),
                sale.getSaleDate(),
                sale.getTotal(),
                sale.getCustomerName(),
                sale.getPaymentMethod(),
                detailDTOs
        );
    }
}