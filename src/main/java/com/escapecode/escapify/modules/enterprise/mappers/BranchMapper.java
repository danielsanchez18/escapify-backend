package com.escapecode.escapify.modules.enterprise.mappers;

import com.escapecode.escapify.modules.enterprise.dto.BranchDTO;
import com.escapecode.escapify.modules.enterprise.entities.Branch;
import com.escapecode.escapify.modules.enterprise.entities.Company;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    public BranchDTO toDTO (Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setPhoneNumber(branch.getPhoneNumber());
        dto.setAddress(branch.getAddress());
        dto.setCity(branch.getCity());
        dto.setCountry(branch.getCountry());
        dto.setLogoUrl(branch.getLogoUrl());

        if (branch.getCompany() != null) {
            dto.setCompanyId(branch.getCompany().getId());
            dto.setCompanyName(branch.getCompany().getName());
        }

        dto.setEnabled(branch.getEnabled());
        dto.setAudit(branch.getAudit());
        dto.setDeleted(branch.getDeleted());
        return dto;
    }

    public Branch toEntity (BranchDTO dto) {
        Branch branch = new Branch();
        branch.setId(dto.getId());
        branch.setName(dto.getName());
        branch.setPhoneNumber(dto.getPhoneNumber());
        branch.setAddress(dto.getAddress());
        branch.setCity(dto.getCity());
        branch.setCountry(dto.getCountry());
        branch.setLogoUrl(dto.getLogoUrl());
        branch.setEnabled(dto.getEnabled());
        branch.setDeleted(dto.getDeleted());

        if (dto.getCompanyId() != null) {
            Company company = new Company();
            company.setId(dto.getCompanyId());
            branch.setCompany(company);
        }

        return  branch;
    }

}
