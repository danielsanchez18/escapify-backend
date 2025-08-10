package com.escapecode.escapify.modules.authorization.dto;

import com.escapecode.escapify.shared.model.Audit;
import lombok.Data;

import java.util.UUID;

@Data
public class PermissionDTO {

    private UUID id;
    private String code;
    private String description;
    private Audit audit;

}
