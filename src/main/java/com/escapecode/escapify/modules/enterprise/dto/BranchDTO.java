package com.escapecode.escapify.modules.enterprise.dto;

import com.escapecode.escapify.shared.model.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class BranchDTO {

    private UUID id;
    private String name;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String logoUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID companyId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String companyName;

    private Boolean enabled;
    private Boolean deleted;
    private Audit audit;

}

