package com.mcabrera.SuperMarket.controllers;

import com.mcabrera.SuperMarket.dtos.ProductDTO;
import com.mcabrera.SuperMarket.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("product_id") Long productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable("product_id") Long productId,
            @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.update(productId, productDTO));
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("product_id") Long productId) {
        productService.deleteById(productId);
        return ResponseEntity.ok("Producto eliminado exitosamente");
    }
}