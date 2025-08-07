package com.escapecode.escapify.modules.inventory.mappers;

import com.escapecode.escapify.modules.inventory.dto.VariantDTO;
import com.escapecode.escapify.modules.inventory.entities.Attribute;
import com.escapecode.escapify.modules.inventory.entities.Product;
import com.escapecode.escapify.modules.inventory.entities.Variant;
import org.springframework.stereotype.Component;

@Component
public class VariantMapper {

    public VariantDTO toDTO (Variant variant) {
        VariantDTO dto = new VariantDTO();
        dto.setId(variant.getId());
        dto.setName(variant.getName());
        dto.setDescription(variant.getDescription());
        dto.setSku(variant.getSku());
        dto.setImageUrl(variant.getImageUrl());

        if (variant.getProduct() != null) {
            dto.setProductId(variant.getProduct().getId());
            dto.setProductName(variant.getProduct().getName());
        }

        if (variant.getAttribute() != null) {
            dto.setAttributeId(variant.getAttribute().getId());
            dto.setAttributeName(variant.getAttribute().getName());
        }

        dto.setEnabled(variant.getEnabled());
        dto.setDeleted(variant.getDeleted());
        dto.setAudit(variant.getAudit());

        return dto;
    }

    public Variant toEntity (VariantDTO dto) {
        Variant variant = new Variant();
        variant.setId(dto.getId());
        variant.setName(dto.getName());
        variant.setDescription(dto.getDescription());
        variant.setSku(dto.getSku());
        variant.setImageUrl(dto.getImageUrl());
        variant.setEnabled(dto.getEnabled());
        variant.setDeleted(dto.getDeleted());

        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            variant.setProduct(product);
        }

        if (dto.getAttributeId() != null) {
            Attribute attribute = new Attribute();
            attribute.setId(dto.getAttributeId());
            variant.setAttribute(attribute);
        }

        return variant;
    }

}
