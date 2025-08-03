package com.escapecode.escapify.modules.enterprise.validators;

import com.escapecode.escapify.modules.enterprise.dto.BranchDTO;
import com.escapecode.escapify.modules.enterprise.entities.Branch;
import com.escapecode.escapify.modules.enterprise.repositories.BranchRepository;
import com.escapecode.escapify.modules.enterprise.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class BranchValidator {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\- ]{7,20}$");

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CompanyRepository companyRepository;

    /*  Validaciones para crear la entidad Branch:
    1. Que los campos obligatorios no estén vacíos.
    2. Que la empresa a la que pertenece la sucursal exista o esté activa ('enabled = true' y 'deleted = false').
    3. Que el nombre de la sucursal no esté repetido en una misma empresa (solo se puede repetir si una sucursal está marcada como 'deleted'). */

    public void validateCreate(BranchDTO dto) {

        // 1. Campos obligatorios
        if (!StringUtils.hasText(dto.getName())) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (!StringUtils.hasText(dto.getAddress())) {
            throw new IllegalArgumentException("El campo dirección es obligatorio.");
        }
        if (!StringUtils.hasText(dto.getCity())) {
            throw new IllegalArgumentException("El campo ciudad es obligatorio.");
        }
        if (!StringUtils.hasText(dto.getCountry())) {
            throw new IllegalArgumentException("El campo país es obligatorio.");
        }

        // 2. Empresa existente y activa
        if (!companyRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getCompanyId())) {
            throw new IllegalArgumentException("La empresa a la que pertenece la sucursal no existe o no está activa.");
        }

        // 3. Nombre no repetido en la misma empresa
        if (branchRepository.existsByNameAndCompanyIdAndDeletedFalse(dto.getName(), dto.getCompanyId())) {
            throw new IllegalArgumentException("Ya existe una sucursal con ese nombre en la misma empresa.");
        }

        // 4. Formato de número de teléfono válido
        if (dto.getPhoneNumber() != null && !PHONE_PATTERN.matcher(dto.getPhoneNumber()).matches()) {
            throw new IllegalArgumentException("Formato de número de teléfono inválido.");
        }
    }

    /*   Validaciones para actualizar la entidad Branch:
    1. Que la sucursal exista, no esté eliminada y esté activa ('enabled = true').
    2. Que los campos obligatorios no estén vacíos.
    3. Que la empresa a la que pertenece la sucursal exista o esté activa ('enabled' y 'deleted' en true).
    4. Que el nombre de la sucursal no esté repetido en una misma empresa (solo se puede repetir si una sucursal está marcada como 'deleted').
    5. Que el número de teléfono tenga un formato válido. */

    public void validateUpdate(UUID id, BranchDTO dto) {
        Branch branch = branchRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("La sucursal no existe o está eliminada."));
        if (Boolean.TRUE.equals(branch.getDeleted())) {
            throw new IllegalArgumentException("La sucursal está eliminada.");
        }
        if (!Boolean.TRUE.equals(branch.getEnabled())) {
            throw new IllegalArgumentException("La sucursal debe estar activa para ser actualizada.");
        }

        String name = StringUtils.hasText(dto.getName()) ? dto.getName() : branch.getName();
        String address = StringUtils.hasText(dto.getAddress()) ? dto.getAddress() : branch.getAddress();
        String city = StringUtils.hasText(dto.getCity()) ? dto.getCity() : branch.getCity();
        String country = StringUtils.hasText(dto.getCountry()) ? dto.getCountry() : branch.getCountry();
        UUID companyId = dto.getCompanyId() != null ? dto.getCompanyId() : branch.getCompany().getId();

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (!StringUtils.hasText(address)) {
            throw new IllegalArgumentException("El campo dirección es obligatorio.");
        }
        if (!StringUtils.hasText(city)) {
            throw new IllegalArgumentException("El campo ciudad es obligatorio.");
        }
        if (!StringUtils.hasText(country)) {
            throw new IllegalArgumentException("El campo país es obligatorio.");
        }

        if (!companyRepository.existsByIdAndEnabledTrueAndDeletedFalse(companyId)) {
            throw new IllegalArgumentException("La empresa a la que pertenece la sucursal no existe o no está activa.");
        }

        Optional<Branch> other = branchRepository.findByNameAndCompanyIdAndDeletedFalse(name, companyId);
        if (other.isPresent() && !other.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe una sucursal con ese nombre en la misma empresa.");
        }

        if (dto.getPhoneNumber() != null && !PHONE_PATTERN.matcher(dto.getPhoneNumber()).matches()) {
            throw new IllegalArgumentException("Formato de número de teléfono inválido.");
        }
    }

}
