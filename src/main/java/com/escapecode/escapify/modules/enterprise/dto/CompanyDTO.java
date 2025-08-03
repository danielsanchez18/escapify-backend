package com.escapecode.escapify.modules.enterprise.dto;

import com.escapecode.escapify.shared.model.Audit;
import lombok.Data;

import java.util.UUID;

@Data
public class CompanyDTO {

    private UUID id;
    private String name;
    private String description;
    private String tag;
    private String phoneNumber;
    private String country;
    private String email;
    private String website;
    private String logoUrl;
    private Boolean enabled;
    private Boolean deleted;
    private Audit audit;

}
