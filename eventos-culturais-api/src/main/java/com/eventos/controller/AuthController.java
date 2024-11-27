package com.eventos.controller;

import com.eventos.dto.LoginDTO;
import com.eventos.dto.TokenDTO;
import com.eventos.dto.UsuarioDTO;
import com.eventos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registro(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(authService.registro(usuarioDTO));
    }
}
