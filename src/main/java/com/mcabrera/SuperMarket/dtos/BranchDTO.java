package com.mcabrera.SuperMarket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private Long branchId;
    private String name;
    private String address;
    private String phone;
    private String branchCode;
}