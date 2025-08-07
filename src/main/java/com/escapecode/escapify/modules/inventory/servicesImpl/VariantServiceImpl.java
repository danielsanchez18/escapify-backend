package com.escapecode.escapify.modules.inventory.servicesImpl;

import com.escapecode.escapify.modules.inventory.dto.VariantDTO;
import com.escapecode.escapify.modules.inventory.entities.Attribute;
import com.escapecode.escapify.modules.inventory.entities.Product;
import com.escapecode.escapify.modules.inventory.entities.Variant;
import com.escapecode.escapify.modules.inventory.mappers.VariantMapper;
import com.escapecode.escapify.modules.inventory.repositories.AttributeRepository;
import com.escapecode.escapify.modules.inventory.repositories.ProductRepository;
import com.escapecode.escapify.modules.inventory.repositories.VariantRepository;
import com.escapecode.escapify.modules.inventory.services.VariantService;
import com.escapecode.escapify.modules.inventory.utils.SkuGenerator;
import com.escapecode.escapify.modules.inventory.validators.VariantValidator;
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
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantRepository repository;

    @Autowired
    private VariantMapper mapper;

    @Autowired
    private VariantValidator validator;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private SkuGenerator skuGenerator;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public VariantDTO create(VariantDTO variantDTO, MultipartFile image) throws IOException {

        // Obtener producto y atributo para generar SKU
        Product product = productRepository.findByIdAndDeletedFalse(variantDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado o eliminado"));

        Attribute attribute = attributeRepository.findByIdAndDeletedFalse(variantDTO.getAttributeId())
                .orElseThrow(() -> new IllegalArgumentException("Atributo no encontrado o eliminado"));

        // Generar SKU único
        String sku = skuGenerator.generateUniqueVariantSku(
                product.getSku(),
                attribute.getSku(),
                product.getId(),
                repository
        );

        variantDTO.setSku(sku);
        variantDTO.setEnabled(true);
        variantDTO.setDeleted(false);

        validator.validateCreate(variantDTO);

        Variant variant = mapper.toEntity(variantDTO);

        // Si la imagen no está vacía, almacenamos la imagen
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeImage(image, "IMG_variants");
            variant.setImageUrl(imagePath);  // Asignamos la ruta de la imagen a la variante
        } else {
            // Si no se recibe imagen, asignamos la imagen predeterminada desde el directorio de recursos
            variant.setImageUrl("static/IMG_variants/logo-variante-default.png");
        }

        Variant savedVariant = repository.save(variant);
        return mapper.toDTO(savedVariant);
    }

    @Override
    public VariantDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));
    }

    @Override
    public Page<VariantDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<VariantDTO> search(String name, String sku, UUID productId, UUID subcategoryId, Date startDate, Date endDate, Boolean enabled, Pageable pageable) {
        Page<Variant> variants = repository.search(name, sku, productId, subcategoryId, startDate, endDate, enabled, pageable);
        return variants.map(mapper::toDTO);
    }

    @Override
    public Page<VariantDTO> getByProductId(UUID productId, Pageable pageable) {
        return repository.findAllByProductIdAndDeletedFalse(productId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public VariantDTO update(UUID id, VariantDTO variantDTO, MultipartFile image) throws IOException {
        validator.validateUpdate(id, variantDTO);

        Variant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        // Cambiar el producto asociado si se proporciona un nuevo productId
        if (variantDTO.getProductId() != null) {
            if (variant.getProduct() == null || !variant.getProduct().getId().equals(variantDTO.getProductId()) ) {
                Product product = productRepository.findById(variantDTO.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
                variant.setProduct(product);
            }
        }

        // Cambiar el atributo asociado si se proporciona un nuevo attributeId
        if (variantDTO.getAttributeId() != null) {
            if (variant.getAttribute() == null || !variant.getAttribute().getId().equals(variantDTO.getAttributeId()) ) {
                Attribute attribute = attributeRepository.findById(variantDTO.getAttributeId())
                        .orElseThrow(() -> new IllegalArgumentException("Attribute no encontrado"));
                variant.setAttribute(attribute);
            }
        }

        // Actualizar otros campos si se proporcionan
        if (variantDTO.getName() != null) variant.setName(variantDTO.getName());
        if (variantDTO.getDescription() != null) variant.setDescription(variantDTO.getDescription());
        if (variantDTO.getSku() != null) variant.setSku(variantDTO.getSku());
        if (variantDTO.getEnabled() != null) variant.setEnabled(variantDTO.getEnabled());

        // Sí se proporciona una nueva imagen
        if (image != null && !image.isEmpty()) {
            // Elimina la imagen anterior si existe
            if (variant.getImageUrl() != null) {
                String oldFileName = variant.getImageUrl().replace("IMG_variants/", "");
                fileStorageService.deleteImage(oldFileName, "IMG_variants");
            }

            // Guarda la nueva imagen
            String imagePath = fileStorageService.storeImage(image, "IMG_variants");
            variant.setImageUrl(imagePath);
        }

        repository.save(variant);
        return mapper.toDTO(variant);
    }

    @Override
    public VariantDTO changeStatus(UUID id) {
        Variant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        if (variant.getDeleted()) {
            throw new IllegalArgumentException("No se puede cambiar el estado de una variante eliminada");
        }

        variant.setEnabled(!variant.getEnabled());
        return mapper.toDTO(repository.save(variant));
    }

    @Override
    public void delete(UUID id) {
        Variant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        if (variant.getDeleted()) {
            throw new IllegalArgumentException("La variante ya está eliminada");
        }

        variant.setDeleted(true);
        repository.save(variant);
    }

    @Override
    public void restore(UUID id) {
        Variant variant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        if (!variant.getDeleted()) {
            throw new IllegalArgumentException("La variante no está eliminada");
        }

        variant.setDeleted(false);
        repository.save(variant);
    }
}
