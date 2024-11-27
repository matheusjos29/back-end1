package com.eventos.service;

import com.eventos.domain.Usuario;
import com.eventos.domain.enums.Role;
import com.eventos.dto.LoginDTO;
import com.eventos.dto.TokenDTO;
import com.eventos.dto.UsuarioDTO;
import com.eventos.exception.BusinessException;
import com.eventos.repository.UsuarioRepository;
import com.eventos.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public TokenDTO login(LoginDTO loginDTO) {
        log.info("Tentativa de login para usuário: {}", loginDTO.getEmail());
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getSenha()
            )
        );

        String token = tokenProvider.generateToken(authentication);
        return new TokenDTO(token, "Bearer");
    }

    public UsuarioDTO registro(UsuarioDTO usuarioDTO) {
        log.info("Registrando novo usuário: {}", usuarioDTO.getEmail());
        
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
            .email(usuarioDTO.getEmail())
            .senha(passwordEncoder.encode(usuarioDTO.getSenha()))
            .nome(usuarioDTO.getNome())
            .role(Role.USER)
            .build();

        usuarioRepository.save(usuario);
        
        return usuarioDTO;
    }
}
