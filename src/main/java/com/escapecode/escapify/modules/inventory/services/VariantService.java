package com.escapecode.escapify.modules.inventory.services;

import com.escapecode.escapify.modules.inventory.dto.VariantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public interface VariantService {

    VariantDTO create(VariantDTO variantDTO, MultipartFile image) throws IOException;

    VariantDTO getById(UUID id);

    Page<VariantDTO> getAll(Pageable pageable);

    Page<VariantDTO> search(
            String name,
            String sku,
            UUID productId,
            UUID subcategoryId,
            Date startDate,
            Date endDate,
            Boolean enabled,
            Pageable pageable
    );

    Page<VariantDTO> getByProductId(UUID productId, Pageable pageable);

    VariantDTO update(UUID id, VariantDTO variantDTO, MultipartFile image) throws IOException;

    VariantDTO changeStatus(UUID id);

    void delete(UUID id);

    void restore(UUID id);

}
