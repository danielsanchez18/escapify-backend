package com.escapecode.escapify.modules.inventory.dto;

import com.escapecode.escapify.shared.model.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class AttributeDTO {

    private UUID id;
    private String name;
    private String description;
    private String sku;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID subcategoryId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String subcategoryName;

    private Boolean deleted;
    private Audit audit;

}
