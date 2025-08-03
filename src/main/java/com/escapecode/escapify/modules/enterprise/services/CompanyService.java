package com.escapecode.escapify.modules.enterprise.services;

import com.escapecode.escapify.modules.enterprise.dto.CompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public interface CompanyService {

    CompanyDTO create(CompanyDTO companyDTO, MultipartFile image) throws IOException;

    CompanyDTO getById(UUID id);

    Page<CompanyDTO> getAll(Pageable pageable);

    Page<CompanyDTO> search(
            String name,
            String tag,
            String country,
            Date startDate,
            Date endDate,
            Boolean enabled,
            Pageable pageable
    );

    CompanyDTO update(UUID id, CompanyDTO companyDTO, MultipartFile image) throws IOException;

    CompanyDTO changeStatus(UUID id);

    void delete(UUID id);

    void restore(UUID id);

}
