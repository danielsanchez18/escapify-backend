package com.escapecode.escapify.modules.inventory.servicesImpl;

import com.escapecode.escapify.modules.inventory.dto.AttributeDTO;
import com.escapecode.escapify.modules.inventory.entities.Attribute;
import com.escapecode.escapify.modules.inventory.entities.Subcategory;
import com.escapecode.escapify.modules.inventory.mappers.AttributeMapper;
import com.escapecode.escapify.modules.inventory.repositories.AttributeRepository;
import com.escapecode.escapify.modules.inventory.repositories.SubcategoryRepository;
import com.escapecode.escapify.modules.inventory.services.AttributeService;
import com.escapecode.escapify.modules.inventory.validators.AttributeValidator;
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
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeRepository repository;

    @Autowired
    private AttributeMapper mapper;

    @Autowired
    private AttributeValidator validator;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public AttributeDTO create(AttributeDTO attributeDTO) {

        validator.validateCreate(attributeDTO);

        attributeDTO.setDeleted(false);

        Attribute attribute = mapper.toEntity(attributeDTO);

        Attribute savedAttribute = repository.save(attribute);
        return mapper.toDTO(savedAttribute);
    }

    @Override
    public AttributeDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Atributo no encontrado"));
    }

    @Override
    public Page<AttributeDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<AttributeDTO> search(String name, String sku, UUID subcategoryId, Date startDate, Date endDate, Pageable pageable) {
        Page<Attribute> attributes = repository.search(name, sku, subcategoryId, startDate, endDate, pageable);
        return attributes.map(mapper::toDTO);
    }

    @Override
    public Page<AttributeDTO> getBySubcategoryId(UUID subcategoryId, Pageable pageable) {
        return repository.findAllBySubcategoryIdAndDeletedFalse(subcategoryId, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public AttributeDTO update(UUID id, AttributeDTO attributeDTO) {
        validator.validateUpdate(id, attributeDTO);

        Attribute attribute = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atributo no encontrado"));

        // Cambiar subcategoría si se envía un nuevo subcategoryId
        if (attributeDTO.getSubcategoryId() != null) {
            if (attribute.getSubcategory() == null || !attribute.getSubcategory().getId().equals(attributeDTO.getSubcategoryId()) ) {
                Subcategory subcategory = subcategoryRepository.findById(attributeDTO.getSubcategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Subcategoría no encontrada"));
                attribute.setSubcategory(subcategory);
            }
        }

        // Actualizar otros campos si se proporcionan
        if (attributeDTO.getName() != null) attribute.setName(attributeDTO.getName());
        if (attributeDTO.getDescription() != null) attribute.setDescription(attributeDTO.getDescription());
        if (attributeDTO.getSku() != null) attribute.setSku(attributeDTO.getSku());

        repository.save(attribute);
        return mapper.toDTO(attribute);
    }

    @Override
    public void delete(UUID id) {
        Attribute attribute = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atributo no encontrado"));

        if (attribute.getDeleted()) {
            throw new IllegalArgumentException("El atributo ya está eliminado");
        }

        attribute.setDeleted(true);
        repository.save(attribute);
    }

    @Override
    public void restore(UUID id) {
        Attribute attribute = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atributo no encontrado"));

        if (!attribute.getDeleted()) {
            throw new IllegalArgumentException("El atributo no está eliminado");
        }

        attribute.setDeleted(false);
        repository.save(attribute);
    }
}
