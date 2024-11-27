package com.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    
    @NotNull(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    
    @NotNull(message = "Senha é obrigatória")
    private String senha;
}