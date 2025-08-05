package com.escapecode.escapify.modules.inventory.services;

import com.escapecode.escapify.modules.inventory.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public interface ProductService {

    ProductDTO create(ProductDTO productDTO, MultipartFile image) throws IOException;

    ProductDTO getById(UUID id);

    Page<ProductDTO> getAll(Pageable pageable);

    Page<ProductDTO> search(
            String name,
            String sku,
            UUID subcategoryId,
            UUID categoryId,
            Date startDate,
            Date endDate,
            Boolean enabled,
            Pageable pageable
    );

    Page<ProductDTO> getBySubcategoryId(UUID subcategoryId, Pageable pageable);

    ProductDTO update(UUID id, ProductDTO productDTO, MultipartFile image) throws IOException;

    ProductDTO changeStatus(UUID id);

    void delete(UUID id);

    void restore(UUID id);

}
