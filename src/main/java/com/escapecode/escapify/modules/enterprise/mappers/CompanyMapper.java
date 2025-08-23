package com.escapecode.escapify.modules.enterprise.mappers;

import com.escapecode.escapify.modules.enterprise.dto.CompanyDTO;
import com.escapecode.escapify.modules.enterprise.entities.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyDTO toDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());
        dto.setTag(company.getTag());
        dto.setPhoneNumber(company.getPhoneNumber());
        dto.setCountry(company.getCountry());
        dto.setCurrency(company.getCurrency());
        dto.setEmail(company.getEmail());
        dto.setWebsite(company.getWebsite());
        dto.setLogoUrl(company.getLogoUrl());
        dto.setEnabled(company.getEnabled());
        dto.setAudit(company.getAudit());
        dto.setDeleted(company.getDeleted());
        return dto;
    }

    public Company toEntity(CompanyDTO dto) {
        Company company = new Company();
        company.setId(dto.getId());
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setTag(dto.getTag());
        company.setPhoneNumber(dto.getPhoneNumber());
        company.setCountry(dto.getCountry());
        company.setCurrency(dto.getCurrency());
        company.setEmail(dto.getEmail());
        company.setWebsite(dto.getWebsite());
        company.setLogoUrl(dto.getLogoUrl());
        company.setEnabled(dto.getEnabled());
        company.setDeleted(dto.getDeleted());
        return company;
    }

}
