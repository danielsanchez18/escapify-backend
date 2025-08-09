package com.escapecode.escapify.modules.authorization.dto;

import com.escapecode.escapify.shared.model.Audit;
import lombok.Data;

import java.util.UUID;

@Data
public class RoleDTO {

    private UUID id;
    private String name;
    private String scope;
    private UUID scopeId;
    private Boolean isCustom;
    private Boolean enabled;
    private Boolean deleted;
    private Audit audit;

}
