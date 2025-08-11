package com.escapecode.escapify.modules.authentication.controller;

import com.escapecode.escapify.modules.authentication.dto.AuthRequest;
import com.escapecode.escapify.modules.authentication.dto.AuthResponse;
import com.escapecode.escapify.modules.authentication.dto.CurrentUserResponse;
import com.escapecode.escapify.modules.authentication.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/current-user")
    public ResponseEntity<CurrentUserResponse> getCurrentUser() {
        return ResponseEntity.ok(authenticationService.getCurrentUser());
    }

}

