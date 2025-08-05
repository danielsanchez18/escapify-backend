package com.escapecode.escapify.modules.inventory.mappers;

import com.escapecode.escapify.modules.inventory.dto.AttributeDTO;
import com.escapecode.escapify.modules.inventory.entities.Attribute;
import com.escapecode.escapify.modules.inventory.entities.Subcategory;
import org.springframework.stereotype.Component;

@Component
public class AttributeMapper {

    public AttributeDTO toDTO (Attribute attribute) {
        AttributeDTO dto = new AttributeDTO();
        dto.setId(attribute.getId());
        dto.setName(attribute.getName());
        dto.setDescription(attribute.getDescription());
        dto.setSku(attribute.getSku());

        if (attribute.getSubcategory() != null) {
            dto.setSubcategoryId(attribute.getSubcategory().getId());
            dto.setSubcategoryName(attribute.getSubcategory().getName());
        }

        dto.setDeleted(attribute.getDeleted());
        dto.setAudit(attribute.getAudit());

        return dto;
    }

    public Attribute toEntity (AttributeDTO dto) {
        Attribute attribute = new Attribute();
        attribute.setId(dto.getId());
        attribute.setName(dto.getName());
        attribute.setDescription(dto.getDescription());
        attribute.setSku(dto.getSku());
        attribute.setDeleted(dto.getDeleted());

        if (dto.getSubcategoryId() != null) {
            Subcategory subcategory = new Subcategory();
            subcategory.setId(dto.getSubcategoryId());
            attribute.setSubcategory(subcategory);
        }

        return attribute;
    }

}
