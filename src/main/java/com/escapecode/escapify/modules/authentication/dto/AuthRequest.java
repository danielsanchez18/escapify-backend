package com.escapecode.escapify.modules.authentication.dto;

import lombok.Data;

@Data
public class AuthRequest {

    private String email;
    private String password;
    private boolean rememberMe; // Para decidir si usar refresh token

}
