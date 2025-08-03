package com.escapecode.escapify.modules.enterprise.servicesImpl;

import com.escapecode.escapify.modules.enterprise.dto.CompanyDTO;
import com.escapecode.escapify.modules.enterprise.entities.Company;
import com.escapecode.escapify.modules.enterprise.mappers.CompanyMapper;
import com.escapecode.escapify.modules.enterprise.repositories.CompanyRepository;
import com.escapecode.escapify.modules.enterprise.services.CompanyService;
import com.escapecode.escapify.modules.enterprise.validators.CompanyValidator;
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
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository repository;

    @Autowired
    private CompanyValidator validator;

    @Autowired
    private CompanyMapper mapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public CompanyDTO create(CompanyDTO companyDTO, MultipartFile image) throws IOException {

        validator.validateCreate(companyDTO);

        companyDTO.setEnabled(true);
        companyDTO.setDeleted(false);

        Company company = mapper.toEntity(companyDTO);

        // Si la imagen no está vacía, almacenamos la imagen
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeImage(image, "IMG_companies");
            company.setLogoUrl(imagePath);  // Asignamos la ruta de la imagen a la empresa
        } else {
            // Si no se recibe imagen, asignamos la imagen predeterminada desde el directorio de recursos
            company.setLogoUrl("static/IMG_companies/logo-empresa-default.png");
        }

        Company savedCompany = repository.save(company);
        return mapper.toDTO(savedCompany);
    }

    @Override
    public CompanyDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
    }

    @Override
    public Page<CompanyDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<CompanyDTO> search(String name, String tag, String country, Date startDate, Date endDate, Boolean enabled, Pageable pageable) {
        Page<Company> companies = repository.search(name, tag, country, startDate, endDate, enabled, pageable);
        return companies.map(company -> mapper.toDTO(company));
    }

    @Override
    public CompanyDTO update(UUID id, CompanyDTO companyDTO, MultipartFile image) throws IOException {
        validator.validateUpdate(id, companyDTO);

        Company company = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        // Solo actualiza los campos no nulos del DTO
        if (companyDTO.getName() != null) company.setName(companyDTO.getName());
        if (companyDTO.getDescription() != null) company.setDescription(companyDTO.getDescription());
        if (companyDTO.getTag() != null) company.setTag(companyDTO.getTag());
        if (companyDTO.getCountry() != null) company.setCountry(companyDTO.getCountry());
        if (companyDTO.getEmail() != null) company.setEmail(companyDTO.getEmail());
        if (companyDTO.getPhoneNumber() != null) company.setPhoneNumber(companyDTO.getPhoneNumber());
        if (companyDTO.getWebsite() != null) company.setWebsite(companyDTO.getWebsite());
        if (companyDTO.getEnabled() != null) company.setEnabled(companyDTO.getEnabled());

        // Sí se proporciona una nueva imagen
        if (image != null && !image.isEmpty()) {
            // Elimina la imagen anterior si existe
            if (company.getLogoUrl() != null) {
                String oldFileName = company.getLogoUrl().replace("IMG_companies/", "");
                fileStorageService.deleteImage(oldFileName, "IMG_companies");
            }

            // Guarda la nueva imagen
            String imagePath = fileStorageService.storeImage(image, "IMG_companies");
            company.setLogoUrl(imagePath);
        }

        repository.save(company);
        return mapper.toDTO(company);
    }

    @Override
    public CompanyDTO changeStatus(UUID id) {
        Company existingCompany = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        existingCompany.setEnabled(!existingCompany.getEnabled());
        return mapper.toDTO(repository.save(existingCompany));
    }

    @Override
    public void delete(UUID id) {
        Company existingCompany = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        if (existingCompany.getDeleted()) {
            throw new IllegalArgumentException("La empresa ya está eliminada");
        }

        existingCompany.setDeleted(true);
        mapper.toDTO(repository.save(existingCompany));
    }

    @Override
    public void restore(UUID id) {
        Company existingCompany = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        if (!existingCompany.getDeleted()) {
            throw new IllegalArgumentException("No se puede restablecer una empresa que no ha sido eliminada");
        }

        existingCompany.setDeleted(false);
        mapper.toDTO(repository.save(existingCompany));
    }

}