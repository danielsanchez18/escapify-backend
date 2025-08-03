package com.escapecode.escapify.modules.inventory.services;

import com.escapecode.escapify.modules.inventory.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public interface CategoryService {

    CategoryDTO create(CategoryDTO categoryDTO, MultipartFile image) throws IOException;

    CategoryDTO getById(UUID id);

    Page<CategoryDTO> getAll(Pageable pageable);

    Page<CategoryDTO> search(
            String name,
            String sku,
            UUID branchId,
            UUID companyId,
            Date startDate,
            Date endDate,
            Boolean enabled,
            Pageable pageable
    );

    Page<CategoryDTO> getByBranchId(UUID branchId, Pageable pageable);

    CategoryDTO update(UUID id, CategoryDTO categoryDTO, MultipartFile image) throws IOException;

    CategoryDTO changeStatus(UUID id);

    void delete(UUID id);

    void restore(UUID id);

}
