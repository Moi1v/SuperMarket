package com.mcabrera.SuperMarket.services;

import com.mcabrera.SuperMarket.dtos.ProductDTO;
import com.mcabrera.SuperMarket.entities.Product;
import com.mcabrera.SuperMarket.exceptions.BusinessRuleException;
import com.mcabrera.SuperMarket.exceptions.DuplicateResourceException;
import com.mcabrera.SuperMarket.exceptions.ResourceNotFoundException;
import com.mcabrera.SuperMarket.repositories.IProductRepository;
import com.mcabrera.SuperMarket.repositories.ISaleDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final IProductRepository productRepository;
    private final ISaleDetailRepository saleDetailRepository;

    public ProductService(IProductRepository productRepository, ISaleDetailRepository saleDetailRepository) {
        this.productRepository = productRepository;
        this.saleDetailRepository = saleDetailRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return convertToDTO(product);
    }

    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        if (productRepository.existsByProductCode(productDTO.getProductCode())) {
            throw new DuplicateResourceException("Ya existe un producto con el código: " + productDTO.getProductCode());
        }

        if (productDTO.getPrice().signum() <= 0) {
            throw new BusinessRuleException("El precio debe ser mayor a 0");
        }

        if (productDTO.getStock() < 0) {
            throw new BusinessRuleException("El stock no puede ser negativo");
        }

        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (!existingProduct.getProductCode().equals(productDTO.getProductCode()) &&
                productRepository.existsByProductCode(productDTO.getProductCode())) {
            throw new DuplicateResourceException("Ya existe un producto con el código: " + productDTO.getProductCode());
        }

        if (productDTO.getPrice().signum() <= 0) {
            throw new BusinessRuleException("El precio debe ser mayor a 0");
        }

        if (productDTO.getStock() < 0) {
            throw new BusinessRuleException("El stock no puede ser negativo");
        }

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStock(productDTO.getStock());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setProductCode(productDTO.getProductCode());

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (saleDetailRepository.existsByProductProductId(id)) {
            throw new BusinessRuleException("No se puede eliminar el producto porque está asociado a ventas");
        }

        productRepository.deleteById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getProductCode()
        );
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());
        product.setProductCode(dto.getProductCode());
        return product;
    }
}