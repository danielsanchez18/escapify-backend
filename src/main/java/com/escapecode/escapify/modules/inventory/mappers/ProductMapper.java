package com.escapecode.escapify.modules.inventory.mappers;

import com.escapecode.escapify.modules.inventory.dto.ProductDTO;
import com.escapecode.escapify.modules.inventory.entities.Product;
import com.escapecode.escapify.modules.inventory.entities.Subcategory;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO (Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setSku(product.getSku());
        dto.setImageUrl(product.getImageUrl());

        if (product.getSubcategory() != null) {
            dto.setSubcategoryId(product.getSubcategory().getId());
            dto.setSubcategoryName(product.getSubcategory().getName());
        }

        dto.setEnabled(product.getEnabled());
        dto.setDeleted(product.getDeleted());
        dto.setAudit(product.getAudit());

        return dto;
    }

    public Product toEntity (ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSku(dto.getSku());
        product.setImageUrl(dto.getImageUrl());
        product.setEnabled(dto.getEnabled());
        product.setDeleted(dto.getDeleted());

        if (dto.getSubcategoryId() != null) {
            Subcategory subcategory = new Subcategory();
            subcategory.setId(dto.getSubcategoryId());
            product.setSubcategory(subcategory);
        }

        return product;
    }

}
