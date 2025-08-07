package com.escapecode.escapify.modules.inventory.dto;

import com.escapecode.escapify.shared.model.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class VariantDTO {

    public UUID id;
    public String name;
    public String description;
    public String sku;
    public String imageUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID attributeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String attributeName;

    private Boolean enabled;
    private Boolean deleted;
    private Audit audit;

}
