package com.escapecode.escapify.modules.inventory.servicesImpl;

import com.escapecode.escapify.modules.inventory.dto.SubcategoryDTO;
import com.escapecode.escapify.modules.inventory.entities.Category;
import com.escapecode.escapify.modules.inventory.entities.Subcategory;
import com.escapecode.escapify.modules.inventory.mappers.SubcategoryMapper;
import com.escapecode.escapify.modules.inventory.repositories.CategoryRepository;
import com.escapecode.escapify.modules.inventory.repositories.SubcategoryRepository;
import com.escapecode.escapify.modules.inventory.services.SubcategoryService;
import com.escapecode.escapify.modules.inventory.utils.SkuGenerator;
import com.escapecode.escapify.modules.inventory.validators.SubcategoryValidator;
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
public class SubcategoryServiceImpl implements SubcategoryService {

    @Autowired
    private SubcategoryRepository repository;

    @Autowired
    private SubcategoryValidator validator;

    @Autowired
    private SubcategoryMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkuGenerator skuGenerator;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public SubcategoryDTO create(SubcategoryDTO subcategoryDTO, MultipartFile image) throws IOException {

        // Obtener la categoría para acceder al SKU
        UUID categoryId = subcategoryDTO.getCategoryId();
        Category category = categoryRepository.findByIdAndDeletedFalse(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        UUID branchId = category.getBranch().getId();

        // Generar SKU automáticamente
        String baseSku = skuGenerator.generateSubcategorySku(subcategoryDTO.getName());
        String finalSku = skuGenerator.generateUniqueSubcategorySku(category.getSku(), baseSku, branchId, repository);
        subcategoryDTO.setSku(finalSku);

        subcategoryDTO.setEnabled(true);
        subcategoryDTO.setDeleted(false);

        validator.validateCreate(subcategoryDTO);

        Subcategory subcategory = mapper.toEntity(subcategoryDTO);

        // Si la imagen no está vacía, almacenamos la imagen
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeImage(image, "IMG_subcategories");
            subcategory.setImageUrl(imagePath);  // Asignamos la ruta de la imagen a la sucursal
        } else {
            // Si no se recibe imagen, asignamos la imagen predeterminada desde el directorio de recursos
            subcategory.setImageUrl("static/IMG_subcategories/logo-subcategoría-default.png");
        }

        Subcategory savedSubcategory = repository.save(subcategory);
        return mapper.toDTO(savedSubcategory);
    }

    @Override
    public SubcategoryDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Subcategoría no encontrada"));
    }

    @Override
    public Page<SubcategoryDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<SubcategoryDTO> search(String name, String sku, UUID categoryId, Date startDate, Date endDate, Boolean enabled, Pageable pageable) {
        Page<Subcategory> subcategories = repository.search(name, sku, categoryId, startDate, endDate, enabled, pageable);
        return subcategories.map(mapper::toDTO);
    }

    @Override
    public Page<SubcategoryDTO> getByCategoryId(UUID categoryId, Pageable pageable) {
        return repository.findAllByCategoryIdAndDeletedFalse(categoryId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public SubcategoryDTO update(UUID id, SubcategoryDTO subcategoryDTO, MultipartFile image) throws IOException {
        validator.validateUpdate(id, subcategoryDTO);

        Subcategory subcategory = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subcategoría no encontrada"));

        // Cambiar categoría si se envía un nuevo categoryId
        if (subcategoryDTO.getCategoryId() != null) {
            if (subcategory.getCategory() == null || !subcategory.getCategory().getId().equals(subcategoryDTO.getCategoryId())) {
                Category category = categoryRepository.findById(subcategoryDTO.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
                subcategory.setCategory(category);
            }
        }

        // Solo actualiza los campos no nulo del DTO
        if (subcategoryDTO.getName() != null) subcategory.setName(subcategoryDTO.getName());
        if (subcategoryDTO.getDescription() != null) subcategory.setDescription(subcategoryDTO.getDescription());
        if (subcategoryDTO.getEnabled() != null) subcategory.setEnabled(subcategoryDTO.getEnabled());

        // Sí se proporciona una nueva imagen
        if (image != null && !image.isEmpty()) {
            // Elimina la imagen anterior si existe
            if (subcategory.getImageUrl() != null) {
                String oldFileName = subcategory.getImageUrl().replace("IMG_subcategories/", "");
                fileStorageService.deleteImage(oldFileName, "IMG_subcategories");
            }

            // Guarda la nueva imagen
            String imagePath = fileStorageService.storeImage(image, "IMG_subcategories");
            subcategory.setImageUrl(imagePath);
        }

        repository.save(subcategory);
        return mapper.toDTO(subcategory);
    }

    @Override
    public SubcategoryDTO changeStatus(UUID id) {
        Subcategory subcategory = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subcategoría no encontrada"));

        if (subcategory.getDeleted()) {
            throw new IllegalArgumentException("No se puede cambiar el estado de una subcategoría eliminada");
        }

        subcategory.setEnabled(!subcategory.getEnabled());
        return mapper.toDTO(repository.save(subcategory));
    }

    @Override
    public void delete(UUID id) {
        Subcategory subcategory = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subcategoría no encontrada"));

        if (subcategory.getDeleted()) {
            throw new IllegalArgumentException("La subcategoría ya está eliminada");
        }

        subcategory.setDeleted(true);
        repository.save(subcategory);
    }

    @Override
    public void restore(UUID id) {
        Subcategory subcategory = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subcategoría no encontrada"));

        if (!subcategory.getDeleted()) {
            throw new IllegalArgumentException("No se puede restablecer una subcategoría que no ha sido eliminada");
        }

        subcategory.setDeleted(false);
        repository.save(subcategory);
    }
}
