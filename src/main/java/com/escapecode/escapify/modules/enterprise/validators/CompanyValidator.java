package com.escapecode.escapify.modules.enterprise.validators;

import com.escapecode.escapify.modules.enterprise.dto.CompanyDTO;
import com.escapecode.escapify.modules.enterprise.entities.Company;
import com.escapecode.escapify.modules.enterprise.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class CompanyValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\- ]{7,20}$");
    private static final Pattern WEBSITE_PATTERN = Pattern.compile("^(https?://)?[\\w.-]+\\.[a-zA-Z]{2,}(/.*)?$");

    @Autowired
    private CompanyRepository companyRepository;

    /*  Validaciones para crear la entidad Company:
    1. Que los campos obligatorios no estén vacíos.
    2. Que el nombre de la empresa no esté repetido (solo se puede repetir si una empresa está marcada como 'deleted').
    3. Que el correo electrónico tenga un formato válido.
    4. Que el número de teléfono tenga un formato válido.
    5. Que el sitio web tenga un formato válido. */

    public void validateCreate(CompanyDTO dto) {

        // 1. Campos obligatorios
        if (!StringUtils.hasText(dto.getName())) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (!StringUtils.hasText(dto.getTag())) {
            throw new IllegalArgumentException("El campo tag es obligatorio.");
        }
        if (!StringUtils.hasText(dto.getCountry())) {
            throw new IllegalArgumentException("El campo country es obligatorio.");
        }
        if (!StringUtils.hasText(dto.getCurrency())) {
            throw new IllegalArgumentException("El campo currency tag es obligatorio.");
        }

        // 2. Nombre no repetido (excepto deleted)
        if (companyRepository.existsByNameAndDeletedFalse(dto.getName())) {
            throw new IllegalArgumentException("Ya existe una empresa con ese nombre.");
        }

        // 3. Email válido
        if (dto.getEmail() != null && !EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            throw new IllegalArgumentException("Formato de correo electrónico inválido.");
        }

        // 4. Teléfono válido
        if (dto.getPhoneNumber() != null && !PHONE_PATTERN.matcher(dto.getPhoneNumber()).matches()) {
            throw new IllegalArgumentException("Formato de número de teléfono inválido.");
        }

        // 5. Sitio web válido
        if (dto.getWebsite() != null && !WEBSITE_PATTERN.matcher(dto.getWebsite()).matches()) {
            throw new IllegalArgumentException("Formato de sitio web inválido.");
        }

    }

    /* Validaciones para actualizar la entidad Company:
    1. Que los campos obligatorios no estén vacíos.
    2. Que la empresa exista y que no esté marcada como 'deleted'.
    3. Que el nombre de la empresa no esté repetido (solo se puede repetir si una empresa está marcada 'como deleted').
    4. Que los campos opcionales tengan un formato válido (correo electrónico, número de teléfono, sitio web). */

    public void validateUpdate(UUID id, CompanyDTO dto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La empresa no existe o está eliminada."));
        if (Boolean.TRUE.equals(company.getDeleted())) {
            throw new IllegalArgumentException("La empresa está eliminada.");
        }

        String name = StringUtils.hasText(dto.getName()) ? dto.getName() : company.getName();
        String tag = StringUtils.hasText(dto.getTag()) ? dto.getTag() : company.getTag();

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (!StringUtils.hasText(tag)) {
            throw new IllegalArgumentException("El campo tag es obligatorio.");
        }

        // Nombre no repetido (excepto la misma empresa)
        Optional<Company> other = companyRepository.findByNameAndDeletedFalse(name);
        if (other.isPresent() && !other.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe una empresa con ese nombre.");
        }

        // Formatos válidos solo si se envían
        if (dto.getEmail() != null && !EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            throw new IllegalArgumentException("Formato de correo electrónico inválido.");
        }
        if (dto.getPhoneNumber() != null && !PHONE_PATTERN.matcher(dto.getPhoneNumber()).matches()) {
            throw new IllegalArgumentException("Formato de número de teléfono inválido.");
        }
        if (dto.getWebsite() != null && !WEBSITE_PATTERN.matcher(dto.getWebsite()).matches()) {
            throw new IllegalArgumentException("Formato de sitio web inválido.");
        }
    }

}
