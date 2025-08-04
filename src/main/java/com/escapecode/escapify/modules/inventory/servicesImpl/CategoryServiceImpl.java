package com.escapecode.escapify.modules.inventory.servicesImpl;

import com.escapecode.escapify.modules.enterprise.entities.Branch;
import com.escapecode.escapify.modules.enterprise.repositories.BranchRepository;
import com.escapecode.escapify.modules.inventory.dto.CategoryDTO;
import com.escapecode.escapify.modules.inventory.entities.Category;
import com.escapecode.escapify.modules.inventory.mappers.CategoryMapper;
import com.escapecode.escapify.modules.inventory.repositories.CategoryRepository;
import com.escapecode.escapify.modules.inventory.services.CategoryService;
import com.escapecode.escapify.modules.inventory.validators.CategoryValidator;
import com.escapecode.escapify.shared.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryValidator validator;

    @Autowired
    private CategoryMapper mapper;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public CategoryDTO create(CategoryDTO categoryDTO, MultipartFile image) throws IOException {

        validator.validateCreate(categoryDTO);

        categoryDTO.setEnabled(true);
        categoryDTO.setDeleted(false);

        Category category = mapper.toEntity(categoryDTO);

        // Si la imagen no está vacía, almacenamos la imagen
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeImage(image, "IMG_categories");
            category.setImageUrl(imagePath);  // Asignamos la ruta de la imagen a la sucursal
        } else {
            // Si no se recibe imagen, asignamos la imagen predeterminada desde el directorio de recursos
            category.setImageUrl("static/IMG_categories/logo-categoría-default.png");
        }

        Category savedCategory = repository.save(category);
        return mapper.toDTO(savedCategory);
    }

    @Override
    public CategoryDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
    }

    @Override
    public Page<CategoryDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<CategoryDTO> search(String name, String sku, UUID branchId, UUID companyId, Date startDate, Date endDate, Boolean enabled, Pageable pageable) {
        Page<Category> categories = repository.search(name, sku, branchId, companyId, startDate, endDate, enabled, pageable);
        return categories.map(category -> mapper.toDTO(category));
    }


    @Override
    public Page<CategoryDTO> getByBranchId(UUID branchId, Pageable pageable) {
        return repository.findAllByBranchIdAndDeletedFalse(branchId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public CategoryDTO update(UUID id, CategoryDTO categoryDTO, MultipartFile image) throws IOException {
        validator.validateUpdate(id, categoryDTO);

        Category category = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        // Cambiar sucursal si se envía un nuevo branchId
        if (categoryDTO.getBranchId() != null) {
            if (category.getBranch() == null || !category.getBranch().getId().equals(categoryDTO.getBranchId())) {
                Branch newBranch = branchRepository.findById(categoryDTO.getBranchId())
                        .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));
                category.setBranch(newBranch);
            }
        }

        // Solo actualiza los campos no nulo del DTO
        if (categoryDTO.getName() != null) category.setName(categoryDTO.getName());
        if (categoryDTO.getDescription() != null) category.setDescription(categoryDTO.getDescription());
        if (categoryDTO.getSku() != null) category.setSku(categoryDTO.getSku());
        if (categoryDTO.getEnabled() != null) category.setEnabled(categoryDTO.getEnabled());

        // Sí se proporciona una nueva imagen
        if (image != null && !image.isEmpty()) {
            // Elimina la imagen anterior si existe
            if (category.getImageUrl() != null) {
                String oldFileName = category.getImageUrl().replace("IMG_categories/", "");
                fileStorageService.deleteImage(oldFileName, "IMG_categories");
            }

            // Guarda la nueva imagen
            String imagePath = fileStorageService.storeImage(image, "IMG_categories");
            category.setImageUrl(imagePath);
        }

        repository.save(category);
        return mapper.toDTO(category);
    }

    @Override
    public CategoryDTO changeStatus(UUID id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        if (category.getDeleted()) {
            throw new IllegalArgumentException("No se puede cambiar el estado de una categoría eliminada");
        }

        category.setEnabled(!category.getEnabled());
        return mapper.toDTO(repository.save(category));
    }

    @Override
    public void delete(UUID id) {
        Category existingCategory = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        if (existingCategory.getDeleted()) {
            throw new IllegalArgumentException("La categoría ya está eliminada");
        }

        existingCategory.setDeleted(true);
        mapper.toDTO(repository.save(existingCategory));
    }

    @Override
    public void restore(UUID id) {
        Category existingCategory = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        if (!existingCategory.getDeleted()) {
            throw new IllegalArgumentException("No se puede restablecer una categoría que no ha sido eliminada");
        }

        existingCategory.setDeleted(false);
        mapper.toDTO(repository.save(existingCategory));
    }

}
