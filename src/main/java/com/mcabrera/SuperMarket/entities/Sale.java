package com.mcabrera.SuperMarket.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sales")
public class Sale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "payment_method")
    private String paymentMethod;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SaleDetail> saleDetails = new ArrayList<>();

    public void prePersist() {
        if (saleDate == null) {
            saleDate = LocalDateTime.now();
        }
    }

    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        if (saleDetails != null && !saleDetails.isEmpty()) {
            this.total = saleDetails.stream()
                    .map(SaleDetail::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}