package com.eventos.eventos_culturais_api.service;

import com.eventos.domain.Categoria;
import com.eventos.dto.CategoriaDTO;
import com.eventos.exception.BusinessException;
import com.eventos.repository.CategoriaRepository;
import com.eventos.service.CategoriaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void deveCriarCategoriaComSucesso() {
        // Arrange
        CategoriaDTO dto = CategoriaDTO.builder()
                .nome("Categoria Test")
                .descricao("Descrição Test")
                .build();

        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .build();

        when(categoriaRepository.existsByNome(dto.getNome())).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        Categoria resultado = categoriaService.criarCategoria(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(categoria.getNome(), resultado.getNome());
        assertEquals(categoria.getDescricao(), resultado.getDescricao());
    }

    @Test
    void deveListarTodasCategorias() {
        // Arrange
        List<Categoria> categorias = Arrays.asList(
            new Categoria(1L, "Categoria 1", "Descrição 1", null),
            new Categoria(2L, "Categoria 2", "Descrição 2", null)
        );

        when(categoriaRepository.findAll()).thenReturn(categorias);

        // Act
        List<Categoria> resultado = categoriaService.listarCategorias();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Categoria 1", resultado.get(0).getNome());
    }

    @Test
    void deveBuscarCategoriaPorId() {
        // Arrange
        Categoria categoria = new Categoria(1L, "Categoria Test", "Descrição Test", null);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        Categoria resultado = categoriaService.buscarCategoriaPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(categoria.getNome(), resultado.getNome());
    }

    @Test
    void deveGerarErroCategoriaComNomeExistente() {
        // Arrange
        CategoriaDTO dto = CategoriaDTO.builder()
                .nome("Categoria Existente")
                .descricao("Descrição Test")
                .build();

        when(categoriaRepository.existsByNome(dto.getNome())).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessException.class, () -> categoriaService.criarCategoria(dto));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }
}