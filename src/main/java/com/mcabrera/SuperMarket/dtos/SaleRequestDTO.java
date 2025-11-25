package com.mcabrera.SuperMarket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequestDTO {
    private Long branchId;
    private String customerName;
    private String paymentMethod;
    private List<SaleDetailRequestDTO> saleDetails;
}