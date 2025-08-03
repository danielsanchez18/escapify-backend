package com.escapecode.escapify.modules.enterprise.services;

import com.escapecode.escapify.modules.enterprise.dto.BranchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public interface BranchService {

    BranchDTO create(BranchDTO branchDTO, MultipartFile image) throws IOException;

    BranchDTO getById(UUID id);

    Page<BranchDTO> getAll(Pageable pageable);

    Page<BranchDTO> search(
            String name,
            String address,
            String city,
            String country,
            UUID companyId,
            Date startDate,
            Date endDate,
            Boolean enabled,
            Pageable pageable
    );

    Page<BranchDTO> getByCompanyId(UUID companyId, Pageable pageable);

    BranchDTO update(UUID id, BranchDTO branchDTO, MultipartFile image) throws IOException;

    void delete(UUID id);

    void restore(UUID id);

}
