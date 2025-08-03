package com.escapecode.escapify.modules.inventory.mappers;

import com.escapecode.escapify.modules.enterprise.entities.Branch;
import com.escapecode.escapify.modules.inventory.dto.CategoryDTO;
import com.escapecode.escapify.modules.inventory.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO (Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setSku(category.getSku());
        dto.setImageUrl(category.getImageUrl());

        if (category.getBranch() != null) {
            dto.setBranchId(category.getBranch().getId());
            dto.setBranchName(category.getBranch().getName());
        }

        dto.setEnabled(category.getEnabled());
        dto.setAudit(category.getAudit());
        dto.setDeleted(category.getDeleted());
        return dto;
    }

    public Category toEntity (CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setSku(dto.getSku());
        category.setImageUrl(dto.getImageUrl());
        category.setEnabled(dto.getEnabled());
        category.setDeleted(dto.getDeleted());

        if (dto.getBranchId() != null) {
            Branch branch = new Branch();
            branch.setId(dto.getBranchId());
            category.setBranch(branch);
        }

        return category;
    }

}
