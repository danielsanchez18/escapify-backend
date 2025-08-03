package com.escapecode.escapify.modules.enterprise.servicesImpl;

import com.escapecode.escapify.modules.enterprise.dto.BranchDTO;
import com.escapecode.escapify.modules.enterprise.entities.Branch;
import com.escapecode.escapify.modules.enterprise.entities.Company;
import com.escapecode.escapify.modules.enterprise.mappers.BranchMapper;
import com.escapecode.escapify.modules.enterprise.repositories.BranchRepository;
import com.escapecode.escapify.modules.enterprise.repositories.CompanyRepository;
import com.escapecode.escapify.modules.enterprise.services.BranchService;
import com.escapecode.escapify.modules.enterprise.validators.BranchValidator;
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
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository repository;

    @Autowired
    private BranchValidator validator;

    @Autowired
    private BranchMapper mapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public BranchDTO create(BranchDTO branchDTO, MultipartFile image) throws IOException {

        validator.validateCreate(branchDTO);

        branchDTO.setEnabled(true);
        branchDTO.setDeleted(false);

        Branch branch = mapper.toEntity(branchDTO);

        // Si la imagen no está vacía, almacenamos la imagen
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeImage(image, "IMG_branches");
            branch.setLogoUrl(imagePath);  // Asignamos la ruta de la imagen a la sucursal
        } else {
            // Si no se recibe imagen, asignamos la imagen predeterminada desde el directorio de recursos
            branch.setLogoUrl("static/IMG_branches/logo-sucursal-default.png");
        }

        Branch savedBranch = repository.save(branch);
        return mapper.toDTO(savedBranch);
    }

    @Override
    public BranchDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));
    }

    @Override
    public Page<BranchDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<BranchDTO> search(String name, String address, String city, String country, UUID companyId, Date startDate, Date endDate, Boolean enabled, Pageable pageable) {
        Page<Branch> branches = repository.search(name, address, city, country, companyId, startDate, endDate, enabled, pageable);
        return branches.map(branch -> mapper.toDTO(branch));
    }

    @Override
    public Page<BranchDTO> getByCompanyId(UUID companyId, Pageable pageable) {
        Page<Branch> branches = repository.findByCompanyIdAndDeletedFalse(companyId, pageable);
        return branches.map(branch -> mapper.toDTO(branch));
    }

    @Override
    public BranchDTO update(UUID id, BranchDTO branchDTO, MultipartFile image) throws IOException {
        validator.validateUpdate(id, branchDTO);

        Branch branch = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));

        // Cambiar empresa si se envía un nuevo companyId
        if (branchDTO.getCompanyId() != null) {
            if (branch.getCompany() == null || !branch.getCompany().getId().equals(branchDTO.getCompanyId())) {
                Company newCompany = companyRepository.findById(branchDTO.getCompanyId())
                        .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
                branch.setCompany(newCompany);
            }
        }

        // Solo actualiza los campos no nulos del DTO
        if (branchDTO.getName() != null) branch.setName(branchDTO.getName());
        if (branchDTO.getPhoneNumber() != null) branch.setPhoneNumber(branchDTO.getPhoneNumber());
        if (branchDTO.getAddress() != null) branch.setAddress(branchDTO.getAddress());
        if (branchDTO.getCity() != null) branch.setCity(branchDTO.getCity());
        if (branchDTO.getCountry() != null) branch.setCountry(branchDTO.getCountry());
        if (branchDTO.getEnabled() != null) branch.setEnabled(branchDTO.getEnabled());

        // Sí se proporciona una nueva imagen
        if (image != null && !image.isEmpty()) {
            // Elimina la imagen anterior si existe
            if (branch.getLogoUrl() != null) {
                String oldFileName = branch.getLogoUrl().replace("IMG_branches/", "");
                fileStorageService.deleteImage(oldFileName, "IMG_branches");
            }

            // Guarda la nueva imagen
            String imagePath = fileStorageService.storeImage(image, "IMG_branches");
            branch.setLogoUrl(imagePath);
        }

        repository.save(branch);
        return mapper.toDTO(branch);
    }

    @Override
    public BranchDTO changeStatus(UUID id) {
        Branch existingBranch = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));

        if (existingBranch.getDeleted()) {
            throw new IllegalArgumentException("No se puede cambiar el estado de una sucursal eliminada");
        }

        existingBranch.setEnabled(!existingBranch.getEnabled());
        return mapper.toDTO(repository.save(existingBranch));
    }

    @Override
    public void delete(UUID id) {
        Branch existingBranch = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));

        if (existingBranch.getDeleted()) {
            throw new IllegalArgumentException("La sucursal ya está eliminada");
        }

        existingBranch.setDeleted(true);
        mapper.toDTO(repository.save(existingBranch));
    }

    @Override
    public void restore(UUID id) {
        Branch existingBranch = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));

        if (!existingBranch.getDeleted()) {
            throw new IllegalArgumentException("No se puede restablecer una sucursal que no ha sido eliminada");
        }

        existingBranch.setDeleted(false);
        mapper.toDTO(repository.save(existingBranch));
    }
}
