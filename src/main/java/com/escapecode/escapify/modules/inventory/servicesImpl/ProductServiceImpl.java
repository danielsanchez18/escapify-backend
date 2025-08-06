package com.escapecode.escapify.modules.inventory.servicesImpl;

import com.escapecode.escapify.modules.inventory.dto.ProductDTO;
import com.escapecode.escapify.modules.inventory.entities.Product;
import com.escapecode.escapify.modules.inventory.entities.Subcategory;
import com.escapecode.escapify.modules.inventory.mappers.ProductMapper;
import com.escapecode.escapify.modules.inventory.repositories.ProductRepository;
import com.escapecode.escapify.modules.inventory.repositories.SubcategoryRepository;
import com.escapecode.escapify.modules.inventory.services.ProductService;
import com.escapecode.escapify.modules.inventory.utils.SkuGenerator;
import com.escapecode.escapify.modules.inventory.validators.ProductValidator;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ProductValidator validator;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private SkuGenerator skuGenerator;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ProductDTO create(ProductDTO productDTO, MultipartFile image) throws IOException {

        Subcategory subcategory = subcategoryRepository.findByIdAndDeletedFalse(productDTO.getSubcategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Subcategoría no encontrada"));

        String baseSku = skuGenerator.generateProductSku(productDTO.getName());
        String uniqueSku = skuGenerator.generateUniqueProductSku(
                subcategory.getSku(), // Usa el SKU de la subcategoría como prefijo
                baseSku,
                subcategory.getCategory().getBranch().getId(), // la sucursal
                repository
        );

        productDTO.setSku(uniqueSku);
        productDTO.setEnabled(true);
        productDTO.setDeleted(false);

        validator.validateCreate(productDTO);

        Product product = mapper.toEntity(productDTO);

        // Si la imagen no está vacía, almacenamos la imagen
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeImage(image, "IMG_products");
            product.setImageUrl(imagePath);  // Asignamos la ruta de la imagen al producto
        } else {
            // Si no se recibe imagen, asignamos la imagen predeterminada desde el directorio de recursos
            product.setImageUrl("static/IMG_products/logo-producto-default.png");
        }

        Product savedProduct = repository.save(product);
        return mapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<ProductDTO> search(String name, String sku, UUID subcategoryId, UUID categoryId, Date startDate, Date endDate, Boolean enabled, Pageable pageable) {
        Page<Product> products = repository.search(name, sku, subcategoryId, categoryId, startDate, endDate, enabled, pageable);
        return products.map(mapper::toDTO);
    }

    @Override
    public Page<ProductDTO> getBySubcategoryId(UUID subcategoryId, Pageable pageable) {
        return repository.findAllBySubcategoryIdAndDeletedFalse(subcategoryId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public ProductDTO update(UUID id, ProductDTO productDTO, MultipartFile image) throws IOException {
        validator.validateUpdate(id, productDTO);

        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Cambiar la subcategoría si se envía un nuevo subcategoryId
        if (productDTO.getSubcategoryId() != null) {
            if (product.getSubcategory() == null || !product.getSubcategory().getId().equals(productDTO.getSubcategoryId()) ) {
                Subcategory subcategory = subcategoryRepository.findById(productDTO.getSubcategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Subcategoría no encontrada"));
                product.setSubcategory(subcategory);
            }
        }

        // Actualizar otros campos si se proporcionan
        if (productDTO.getName() != null) product.setName(productDTO.getName());
        if (productDTO.getDescription() != null) product.setDescription(productDTO.getDescription());
        if (productDTO.getEnabled() != null) product.setEnabled(productDTO.getEnabled());

        // Sí se proporciona una nueva imagen
        if (image != null && !image.isEmpty()) {
            // Elimina la imagen anterior si existe
            if (product.getImageUrl() != null) {
                String oldFileName = product.getImageUrl().replace("IMG_products/", "");
                fileStorageService.deleteImage(oldFileName, "IMG_products");
            }

            // Guarda la nueva imagen
            String imagePath = fileStorageService.storeImage(image, "IMG_products");
            product.setImageUrl(imagePath);
        }

        repository.save(product);
        return mapper.toDTO(product);
    }

    @Override
    public ProductDTO changeStatus(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (product.getDeleted()) {
            throw new IllegalArgumentException("No se puede cambiar el estado de un producto eliminado");
        }

        product.setEnabled(!product.getEnabled());
        return mapper.toDTO(repository.save(product));
    }

    @Override
    public void delete(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (product.getDeleted()) {
            throw new IllegalArgumentException("El producto ya está eliminado");
        }

        product.setDeleted(true);
        repository.save(product);
    }

    @Override
    public void restore(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (!product.getDeleted()) {
            throw new IllegalArgumentException("No se puede restablecer un producto que no ha sido eliminado");
        }

        product.setDeleted(false);
        repository.save(product);
    }

}
