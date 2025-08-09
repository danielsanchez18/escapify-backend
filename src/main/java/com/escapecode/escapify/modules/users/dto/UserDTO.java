package com.escapecode.escapify.modules.users.dto;

import com.escapecode.escapify.shared.model.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID id;
    private String name;
    private String lastname;
    private String phoneNumber;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String photoUrl;
    private String provider;
    private String providerId;
    private Boolean enabled;
    private Boolean deleted;
    private Audit audit;

}