package com.eventos.eventos_culturais_api.service;

import com.eventos.domain.Usuario;
import com.eventos.dto.LoginDTO;
import com.eventos.dto.TokenDTO;
import com.eventos.dto.UsuarioDTO;
import com.eventos.exception.BusinessException;
import com.eventos.repository.UsuarioRepository;
import com.eventos.security.JwtTokenProvider;
import com.eventos.service.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveRealizarLoginComSucesso() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("user@test.com", "password123");
        Authentication authentication = mock(Authentication.class);
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("token-test");

        // Act
        TokenDTO resultado = authService.login(loginDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("token-test", resultado.getToken());
        assertEquals("Bearer", resultado.getTipo());
    }

    @Test
    void deveRegistrarNovoUsuarioComSucesso() {
        // Arrange
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .email("novo@test.com")
                .senha("senha123")
                .nome("Novo Usuario")
                .build();

        when(usuarioRepository.existsByEmail(usuarioDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        // Act
        UsuarioDTO resultado = authService.registro(usuarioDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuarioDTO.getEmail(), resultado.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveGerarErroAoRegistrarUsuarioComEmailExistente() {
        // Arrange
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .email("existente@test.com")
                .senha("senha123")
                .nome("Usuario Existente")
                .build();

        when(usuarioRepository.existsByEmail(usuarioDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessException.class, () -> authService.registro(usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}