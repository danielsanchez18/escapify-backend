package com.escapecode.escapify.modules.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CurrentUserResponse {

    private String id;
    private String name;
    private String lastname;
    private String email;
    private List<String> roles;
    private List<String> permissions;

}
