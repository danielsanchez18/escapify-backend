package com.escapecode.escapify.modules.inventory.validators;

import com.escapecode.escapify.modules.inventory.dto.SubcategoryDTO;
import com.escapecode.escapify.modules.inventory.entities.Category;
import com.escapecode.escapify.modules.inventory.entities.Subcategory;
import com.escapecode.escapify.modules.inventory.repositories.CategoryRepository;
import com.escapecode.escapify.modules.inventory.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class SubcategoryValidator {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /* Validaciones para crear la entidad Subcategory.
    1. Validar que los campos obligatorios no sean nulos o vacíos.
    2. Validar que la categoría a la que pertenece exista o esté activa ('enabled = true' y 'deleted = false').
    3. Validar que el nombre no se repita en una misma categoría (solo se puede repetir si una categoría está marcada como 'deleted').
    4. Validar que el sku no se repita en una misma categoría (solo se puede repetir si una categoría está marcada como 'deleted'). */

    public void validateCreate(SubcategoryDTO dto) {

        // 1. Validar campos obligatorios
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (dto.getSku() == null || dto.getSku().isEmpty()) {
            throw new IllegalArgumentException("El campo sku es obligatorio.");
        }

        // 2. Validar que la categoría exista y esté activa
        Category category = categoryRepository.findById(dto.getCategoryId())
                .filter(cat -> Boolean.TRUE.equals(cat.getEnabled()) && Boolean.FALSE.equals(cat.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe o no está activa."));

        UUID branchId = category.getBranch().getId();

        // 3. Validar nombre único en la misma categoría
        if (subcategoryRepository.existsByNameAndCategoryIdAndBranchId(dto.getName(), dto.getCategoryId(), branchId)) {
            throw new IllegalArgumentException("Ya existe una subcategoría con ese nombre en la misma categoría.");
        }

        // 4. Validar sku único en la misma categoría
        if (subcategoryRepository.existsBySkuAndCategoryIdAndBranchId(dto.getSku(), dto.getCategoryId(), branchId)) {
            throw new IllegalArgumentException("Ya existe una subcategoría con ese sku en la misma categoría.");
        }
    }

    /* Validaciones para actualizar la entidad Subcategory
    1. Validar que la subcategoría exista, no esté eliminada y esté activa.
    2. Validar que los campos obligatorios no sean nulos o vacíos.
    3. Validar que la categoría a la que pertenece exista o esté activa ('enabled = true' y 'deleted = false').
    4. Validar que el nombre no se repita en una misma categoría (solo se puede repetir si una categoría está marcada como 'deleted').
    5. Validar que el sku no se repita en una misma categoría (solo se puede repetir si una categoría está marcada como 'deleted'). */

    public void validateUpdate(UUID id, SubcategoryDTO dto) {

        // 1. Validar que la subcategoría exista, no esté eliminada y esté activa
        Subcategory subcategory = subcategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La subcategoría con ese ID no existe."));

        if (Boolean.TRUE.equals(subcategory.getDeleted())) {
            throw new IllegalArgumentException("No se puede actualizar una subcategoría eliminada.");
        }

        if (Boolean.FALSE.equals(subcategory.getEnabled())) {
            throw new IllegalArgumentException("La subcategoría no está activa.");
        }

        // 2. Validar campos obligatorios
        String name = StringUtils.hasText(dto.getName()) ? dto.getName() : subcategory.getName();
        String sku = StringUtils.hasText(dto.getSku()) ? dto.getSku() : subcategory.getSku();
        UUID categoryId = dto.getCategoryId() != null ? dto.getCategoryId() : subcategory.getCategory().getId();

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }

        if (!StringUtils.hasText(sku)) {
            throw new IllegalArgumentException("El campo sku es obligatorio.");
        }

        // 3. Validar que la categoría exista y esté activa
        Category category = categoryRepository.findById(categoryId)
                .filter(cat -> Boolean.TRUE.equals(cat.getEnabled()) && Boolean.FALSE.equals(cat.getDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("La categoría no existe o no está activa."));

        UUID branchId = category.getBranch().getId();

        // 4. Validar nombre único en la misma categoría
        if (subcategoryRepository.existsByNameAndCategoryIdAndBranchId(name, categoryId, branchId)) {
            if (!subcategory.getName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("Ya existe una subcategoría con ese nombre en la misma categoría y sucursal.");
            }
        }
    }

}
