package com.mcabrera.SuperMarket.repositories;

import com.mcabrera.SuperMarket.entities.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISaleDetailRepository extends JpaRepository<SaleDetail, Long> {

    boolean existsByProductProductId(Long productId);

    @Query("SELECT sd.product.productId, sd.product.name, sd.product.category, SUM(sd.quantity) as totalSold " +
            "FROM SaleDetail sd " +
            "GROUP BY sd.product.productId, sd.product.name, sd.product.category " +
            "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts(@Param("limit") int limit);
}