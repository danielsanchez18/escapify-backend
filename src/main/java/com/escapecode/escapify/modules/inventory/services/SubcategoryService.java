package com.escapecode.escapify.modules.inventory.services;

import com.escapecode.escapify.modules.inventory.dto.SubcategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public interface SubcategoryService {

    SubcategoryDTO create(SubcategoryDTO subcategoryDTO, MultipartFile image) throws IOException;

    SubcategoryDTO getById(UUID id);

    Page<SubcategoryDTO> getAll(Pageable pageable);

    Page<SubcategoryDTO> search(
            String name,
            String sku,
            UUID categoryId,
            Date startDate,
            Date endDate,
            Boolean enabled,
            Pageable pageable
    );

    Page<SubcategoryDTO> getByCategoryId(UUID categoryId, Pageable pageable);

    SubcategoryDTO update(UUID id, SubcategoryDTO subcategoryDTO, MultipartFile image) throws IOException;

    SubcategoryDTO changeStatus(UUID id);

    void delete(UUID id);

    void restore(UUID id);

}
