package com.escapecode.escapify.modules.inventory.validators;

import com.escapecode.escapify.modules.inventory.dto.VariantDTO;
import com.escapecode.escapify.modules.inventory.entities.Variant;
import com.escapecode.escapify.modules.inventory.repositories.AttributeRepository;
import com.escapecode.escapify.modules.inventory.repositories.ProductRepository;
import com.escapecode.escapify.modules.inventory.repositories.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class VariantValidator {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    /* Validaciones para crear la entidad Variant.
    1. Validar que los campos obligatorios no sean nulos o vacíos.
    2. Validar que el producto al que pertenece exista o esté activo ('enabled = true' y 'deleted = false').
    3. Validar que el atributo al que pertenece exista ('deleted = false').
    4. Validar que el nombre no se repita en un mismo producto (solo se puede repetir si un producto está marcado como 'deleted').
    5. Validar que el sku no se repita en un mismo producto (solo se puede repetir si un producto está marcado como 'deleted'). */

    public void validateCreate(VariantDTO dto) {

        // 1. Validar campos obligatorios
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (dto.getSku() == null || dto.getSku().isEmpty()) {
            throw new IllegalArgumentException("El campo sku es obligatorio.");
        }

        // 2. Validar que el producto exista y esté activo
        if (dto.getProductId() == null || !productRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getProductId())) {
            throw new IllegalArgumentException("El producto al que pertenece la variante no existe o no está activo.");
        }

        // 3. Validar que el atributo exista y no esté eliminado
        if (dto.getAttributeId() == null || !attributeRepository.existsByIdAndDeletedFalse(dto.getAttributeId())) {
            throw new IllegalArgumentException("El atributo al que pertenece la variante no existe o está eliminado.");
        }

        // 4. Validar nombre único en el mismo producto
        if (variantRepository.existsByNameAndProductIdAndDeletedFalse(dto.getName(), dto.getProductId())) {
            throw new IllegalArgumentException("Ya existe una variante con ese nombre en el mismo producto.");
        }

        // 5. Validar sku único en el mismo producto
        if (variantRepository.existsBySkuAndProductId(dto.getSku(), dto.getProductId())) {
            throw new IllegalArgumentException("Ya existe una variante con ese sku en el mismo producto.");
        }
    }

    /* Validaciones para actualizar la entidad Variant.
    1. Validar que la variante exista, no esté eliminada y esté activa.
    2. Validar que los campos obligatorios no sean nulos o vacíos.
    3. Validar que el producto al que pertenece exista o esté activo ('enabled = true' y 'deleted = false').
    4. Validar que el atributo al que pertenece exista ('deleted = false').
    5. Validar que el nombre no se repita en un mismo producto (solo se puede repetir si un producto está marcado como 'deleted').
    6. Validar que el sku no se repita en un mismo producto (solo se puede repetir si un producto está marcado como 'deleted'). */

    public void validateUpdate(UUID id, VariantDTO dto) {

        // 1. Validar que la variante exista, no esté eliminada y esté activa
        var variant = variantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La variante con ese ID no existe."));

        if (Boolean.TRUE.equals(variant.getDeleted())) {
            throw new IllegalArgumentException("No se puede actualizar una variante eliminada.");
        }

        if (Boolean.FALSE.equals(variant.getEnabled())) {
            throw new IllegalArgumentException("La variante no está activa.");
        }

        // 2. Validar campos obligatorios
        String name = StringUtils.hasText(dto.getName()) ? dto.getName() : variant.getName();
        String sku = StringUtils.hasText(dto.getSku()) ? dto.getSku() : variant.getSku();
        UUID productId = dto.getProductId() != null ? dto.getProductId() : variant.getProduct().getId();
        UUID attributeId = dto.getAttributeId() != null ? dto.getAttributeId() : variant.getAttribute().getId();

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (!StringUtils.hasText(sku)) {
            throw new IllegalArgumentException("El campo sku es obligatorio.");
        }

        // 3. Validar que el producto exista y esté activo
        if (productId == null || !productRepository.existsByIdAndEnabledTrueAndDeletedFalse(productId)) {
            throw new IllegalArgumentException("El producto al que pertenece la variante no existe o no está activo.");
        }

        // 4. Validar que el atributo exista y no esté eliminado
        if (attributeId == null || !attributeRepository.existsByIdAndDeletedFalse(attributeId)) {
            throw new IllegalArgumentException("El atributo al que pertenece la variante no existe o está eliminado.");
        }

        // 5. Validar nombre único en el mismo producto
        Optional<Variant> existingName = variantRepository.findByNameAndProductIdAndDeletedFalse(name, productId);
        if (existingName.isPresent() && !existingName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe una variante con ese nombre en el mismo producto.");
        }

    }

}
