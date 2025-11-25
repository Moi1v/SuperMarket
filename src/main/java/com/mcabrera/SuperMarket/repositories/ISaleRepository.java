package com.mcabrera.SuperMarket.repositories;

import com.mcabrera.SuperMarket.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s LEFT JOIN FETCH s.saleDetails sd LEFT JOIN FETCH sd.product WHERE s.branch.branchId = :branchId AND s.saleDate BETWEEN :start AND :end")
    List<Sale> findByBranchAndDateRange(
            @Param("branchId") Long branchId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT s FROM Sale s LEFT JOIN FETCH s.saleDetails WHERE s.branch.branchId = :branchId")
    List<Sale> findByBranchBranchId(@Param("branchId") Long branchId);
}