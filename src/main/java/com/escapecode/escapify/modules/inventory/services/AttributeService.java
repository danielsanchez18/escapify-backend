package com.escapecode.escapify.modules.inventory.services;

import com.escapecode.escapify.modules.inventory.dto.AttributeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public interface AttributeService {

    AttributeDTO create(AttributeDTO attributeDTO);

    AttributeDTO getById(UUID id);

    Page<AttributeDTO> getAll(Pageable pageable);

    Page<AttributeDTO> search(
            String name,
            String sku,
            UUID subcategoryId,
            Date startDate,
            Date endDate,
            Pageable pageable
    );

    Page<AttributeDTO> getBySubcategoryId(UUID subcategoryId, Pageable pageable);

    AttributeDTO update(UUID id, AttributeDTO attributeDTO);

    void delete(UUID id);

    void restore(UUID id);

}
