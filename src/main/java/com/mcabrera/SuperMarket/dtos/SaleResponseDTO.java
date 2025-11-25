package com.mcabrera.SuperMarket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDTO {
    private Long saleId;
    private Long branchId;
    private String branchName;
    private LocalDateTime saleDate;
    private BigDecimal total;
    private String customerName;
    private String paymentMethod;
    private List<SaleDetailResponseDTO> saleDetails;
}