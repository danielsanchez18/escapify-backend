package com.escapecode.escapify.modules.inventory.validators;

import com.escapecode.escapify.modules.inventory.dto.AttributeDTO;
import com.escapecode.escapify.modules.inventory.entities.Attribute;
import com.escapecode.escapify.modules.inventory.repositories.AttributeRepository;
import com.escapecode.escapify.modules.inventory.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class AttributeValidator {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    /* Validaciones para crear la entidad Attribute.
    1. Validar que los campos obligatorios no sean nulos o vacíos.
    2. Validar que la subcategoría a la que pertenece exista o esté activa ('enabled = true' y 'deleted = false').
    3. Validar que el nombre no se repita en una misma subcategoría (solo se puede repetir si una subcategoría está marcada como 'deleted').
    4. Validar que el sku no se repita en una misma subcategoría (solo se puede repetir si una subcategoría está marcada como 'deleted'). */

    public void validateCreate(AttributeDTO dto) {

        // 1. Validar campos obligatorios
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }

        if (dto.getSku() == null || dto.getSku().isEmpty()) {
            throw new IllegalArgumentException("El campo sku es obligatorio.");
        }

        // 2. Validar que la subcategoría exista y esté activa
        if (dto.getSubcategoryId() == null || !subcategoryRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getSubcategoryId())) {
            throw new IllegalArgumentException("La subcategoría a la que pertenece el atributo no existe o no está activa.");
        }

        // 3. Validar nombre único en la misma subcategoría
        if (attributeRepository.existsByNameAndSubcategoryIdAndDeletedFalse(dto.getName(), dto.getSubcategoryId())) {
            throw new IllegalArgumentException("Ya existe un atributo con ese nombre en la misma subcategoría.");
        }

        // 4. Validar sku único en la misma subcategoría
        if (attributeRepository.existsBySkuAndSubcategoryIdAndDeletedFalse(dto.getSku(), dto.getSubcategoryId())) {
            throw new IllegalArgumentException("Ya existe un atributo con ese sku en la misma subcategoría.");
        }
    }

    /* Validaciones para actualizar la entidad Attribute.
    1. Validar que el atributo exista y no esté eliminado.
    2. Validar que los campos obligatorios no sean nulos o vacíos.
    3. Validar que la subcategoría a la que pertenece exista o esté activa ('enabled = true' y 'deleted = false').
    4. Validar que el nombre no se repita en una misma subcategoría (solo se puede repetir si un atributo está marcado como 'deleted').
    5. Validar que el sku no se repita en una misma subcategoría (solo se puede repetir si un atributo está marcado como 'deleted'). */

    public void validateUpdate(UUID id, AttributeDTO dto) {

        // 1. Validar que el atributo exista y no esté eliminado
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El atributo con ese ID no existe."));

        if (Boolean.TRUE.equals(attribute.getDeleted())) {
            throw new IllegalArgumentException("No se puede actualizar un atributo eliminado.");
        }

        // 2. Validar campos obligatorios
        String name = StringUtils.hasText(dto.getName()) ? dto.getName() : attribute.getName();
        String sku = StringUtils.hasText(dto.getSku()) ? dto.getSku() : attribute.getSku();
        UUID subcategoryId = dto.getSubcategoryId() != null ? dto.getSubcategoryId() : attribute.getSubcategory().getId();

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }

        if (!StringUtils.hasText(sku)) {
            throw new IllegalArgumentException("El campo sku es obligatorio.");
        }

        // 3. Validar que la subcategoría exista y esté activa
        if (dto.getSubcategoryId() == null || !subcategoryRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getSubcategoryId())) {
            throw new IllegalArgumentException("La subcategoría a la que pertenece el atributo no existe o no está activa.");
        }

        // 4. Validar nombre único en la misma subcategoría
        Optional<Attribute> existingName = attributeRepository.findByNameAndSubcategoryIdAndDeletedFalse(name, subcategoryId);
        if (existingName.isPresent() && !existingName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe un atributo con ese nombre en la misma subcategoría.");
        }

        // 5. Validar sku único en la misma subcategoría
        Optional<Attribute> existingSku = attributeRepository.findBySkuAndSubcategoryIdAndDeletedFalse(name, subcategoryId);
        if (existingSku.isPresent() && !existingSku.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe un atributo con ese sku en la misma subcategoría.");
        }
    }

}
