package com.mcabrera.SuperMarket.services;

import com.mcabrera.SuperMarket.dtos.TopProductDTO;
import com.mcabrera.SuperMarket.entities.SaleDetail;
import com.mcabrera.SuperMarket.exceptions.ResourceNotFoundException;
import com.mcabrera.SuperMarket.repositories.IBranchRepository;
import com.mcabrera.SuperMarket.repositories.ISaleDetailRepository;
import com.mcabrera.SuperMarket.repositories.ISaleDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ISaleRepository saleRepository;
    private final ISaleDetailRepository saleDetailRepository;
    private final IBranchRepository branchRepository;

    public ReportService(ISaleRepository saleRepository, ISaleDetailRepository saleDetailRepository,
                         IBranchRepository branchRepository) {
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.branchRepository = branchRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getBranchSalesReport(Long branchId, LocalDateTime start, LocalDateTime end) {
        if (!branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Sucursal no encontrada con ID: " + branchId);
        }

        List<Sale> sales = saleRepository.findByBranchAndDateRange(branchId, start, end);

        BigDecimal totalRevenue = sales.stream()
                .map(Sale::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> report = new HashMap<>();
        report.put("branchId", branchId);
        report.put("startDate", start);
        report.put("endDate", end);
        report.put("totalSales", sales.size());
        report.put("totalRevenue", totalRevenue);
        report.put("sales", sales);

        return report;
    }

    @Transactional(readOnly = true)
    public List<TopProductDTO> getTopProducts(int limit) {
        List<Object[]> results = saleDetailRepository.findTopSellingProducts(limit);

        return results.stream()
                .limit(limit)
                .map(result -> new TopProductDTO(
                        (Long) result[0],
                        (String) result[1],
                        (String) result[2],
                        (Long) result[3]
                ))
                .collect(Collectors.toList());
    }
}