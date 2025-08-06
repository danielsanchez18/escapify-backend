package com.escapecode.escapify.modules.inventory.validators;

import com.escapecode.escapify.modules.enterprise.repositories.BranchRepository;
import com.escapecode.escapify.modules.inventory.dto.CategoryDTO;
import com.escapecode.escapify.modules.inventory.entities.Category;
import com.escapecode.escapify.modules.inventory.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class CategoryValidator {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BranchRepository branchRepository;

    /* Validaciones para crear la entidad Category.
    1. Validar que los campos obligatorios no sean nulos o vacíos.
    2. Validar que la sucursal a la que pertenece exista o esté activa ('enabled = true' y 'deleted = false').
    3. Validar que el nombre no se repita en una misma sucursal (solo se puede repetir si una sucursal está marcada como 'deleted').
    4. Validar que el sku no se repita en una misma sucursal. */

    public void validateCreate(CategoryDTO dto) {

        // 1. Validar campos obligatorios
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (dto.getSku() == null || dto.getSku().isEmpty()) {
            throw new IllegalArgumentException("El campo sku es obligatorio.");
        }

        // 2. Validar que la sucursal exista y esté activa
        if (dto.getBranchId() == null || !branchRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getBranchId())) {
            throw new IllegalArgumentException("La sucursal a la que pertenece la categoría no existe o no está activa.");
        }

        // 3. Validar nombre único en la misma sucursal
        if (categoryRepository.existsByNameAndBranchIdAndDeletedFalse(dto.getName(), dto.getBranchId())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre en la misma sucursal.");
        }

        // 4. Validar sku único en la misma sucursal
        if (categoryRepository.existsBySkuAndBranchId(dto.getSku(), dto.getBranchId())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese sku en la misma sucursal.");
        }
    }

    /* Validaciones para actualizar la entidad Category.
    1. Validar que la categoría exista, no esté eliminada y esté activa.
    2. Validar que los campos obligatorios no sean nulos o vacíos.
    3. Validar que la sucursal a la que pertenece exista o esté activa ('enabled = true' y 'deleted = false').
    4. Validar que el nombre no se repita en una misma sucursal (solo se puede repetir si una sucursal está marcada como 'deleted').
    5. Validar que el sku no se repita en una misma sucursal. */

    public void validateUpdate(UUID id, CategoryDTO dto) {

        // 1. Validar que la categoría exista, no esté eliminada y esté activa
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La categoría con ese ID no existe."));

        if (Boolean.TRUE.equals(category.getDeleted())) {
            throw new IllegalArgumentException("La categoría está eliminada.");
        }

        if (Boolean.FALSE.equals(category.getEnabled())) {
            throw new IllegalArgumentException("La categoría no está activa.");
        }

        // 2. Validar campos obligatorios
        String name = StringUtils.hasText(dto.getName()) ? dto.getName() : category.getName();
        String sku = StringUtils.hasText(dto.getSku()) ? dto.getSku() : category.getSku();
        UUID branchId = dto.getBranchId() != null ? dto.getBranchId() : category.getBranch().getId();

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (!StringUtils.hasText(sku)) {
            throw new IllegalArgumentException("El campo sku es obligatorio.");
        }

        // 3. Validar que la sucursal exista y esté activa
        if (branchId == null || !branchRepository.existsByIdAndEnabledTrueAndDeletedFalse(branchId)) {
            throw new IllegalArgumentException("La sucursal a la que pertenece la categoría no existe o no está activa.");
        }

        // 4. Validar nombre único en la misma sucursal
        Optional<Category> otherName = categoryRepository.findByNameAndBranchIdAndDeletedFalse(name, branchId);
        if (otherName.isPresent() && !otherName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre en la misma sucursal.");
        }

        // 5. Validar sku único en la misma sucursal
        Optional<Category> otherSku = categoryRepository.findBySkuAndBranchId(sku, branchId);
        if (otherSku.isPresent() && !otherSku.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe una categoría con ese sku en la misma sucursal.");
        }
    }

}
