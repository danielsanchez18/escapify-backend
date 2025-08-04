package com.escapecode.escapify.modules.inventory.mappers;

import com.escapecode.escapify.modules.inventory.dto.SubcategoryDTO;
import com.escapecode.escapify.modules.inventory.entities.Category;
import com.escapecode.escapify.modules.inventory.entities.Subcategory;
import org.springframework.stereotype.Component;

@Component
public class SubcategoryMapper {

    public SubcategoryDTO toDTO(Subcategory subcategory) {
        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setId(subcategory.getId());
        dto.setName(subcategory.getName());
        dto.setDescription(subcategory.getDescription());
        dto.setSku(subcategory.getSku());
        dto.setImageUrl(subcategory.getImageUrl());

        if (subcategory.getCategory() != null) {
            dto.setCategoryId(subcategory.getCategory().getId());
            dto.setCategoryName(subcategory.getCategory().getName());
        }

        dto.setEnabled(subcategory.getEnabled());
        dto.setDeleted(subcategory.getDeleted());
        dto.setAudit(subcategory.getAudit());

        return dto;
    }

    public Subcategory toEntity(SubcategoryDTO dto) {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(dto.getId());
        subcategory.setName(dto.getName());
        subcategory.setDescription(dto.getDescription());
        subcategory.setSku(dto.getSku());
        subcategory.setImageUrl(dto.getImageUrl());
        subcategory.setEnabled(dto.getEnabled());
        subcategory.setDeleted(dto.getDeleted());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            subcategory.setCategory(category);
        }

        return subcategory;
    }

}
