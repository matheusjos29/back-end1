package com.eventos.eventos_culturais_api.service;

import com.eventos.domain.Categoria;
import com.eventos.domain.Evento;
import com.eventos.dto.EventoDTO;
import com.eventos.exception.BusinessException;
import com.eventos.exception.ResourceNotFoundException;
import com.eventos.repository.CategoriaRepository;
import com.eventos.repository.EventoRepository;
import com.eventos.service.EventoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private EventoService eventoService;

    @Test
    void deveCriarEventoComSucesso() {
        // Arrange
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Categoria Test")
                .descricao("Descrição Test")
                .build();

        EventoDTO dto = EventoDTO.builder()
                .nome("Evento Test")
                .descricao("Descrição Test")
                .dataHora(LocalDateTime.now().plusDays(1))
                .local("Local Test")
                .capacidade(100)
                .categoriaId(1L)
                .gratuito(false)
                .preco(BigDecimal.TEN)
                .build();

        Evento evento = Evento.builder()
                .id(1L)
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .dataHora(dto.getDataHora())
                .local(dto.getLocal())
                .capacidade(dto.getCapacidade())
                .categoria(categoria)
                .gratuito(dto.isGratuito())
                .preco(dto.getPreco())
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        // Act
        Evento resultado = eventoService.criarEvento(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(evento.getNome(), resultado.getNome());
        assertEquals(evento.getPreco(), resultado.getPreco());
    }

    @Test
    void deveBuscarEventosPorNome() {
        // Arrange
        String nomeEvento = "Show";
        List<Evento> eventos = Arrays.asList(
            Evento.builder().id(1L).nome("Show de Rock").build(),
            Evento.builder().id(2L).nome("Show de Jazz").build()
        );

        when(eventoRepository.findByNomeContainingIgnoreCase(nomeEvento)).thenReturn(eventos);

        // Act
        List<Evento> resultado = eventoService.buscarEventosPorNome(nomeEvento);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(e -> e.getNome().contains("Show")));
    }

    @Test
    void deveGerarErroAoCriarEventoComDataPassada() {
        // Arrange
        EventoDTO dto = EventoDTO.builder()
                .nome("Evento Test")
                .descricao("Descrição Test")
                .dataHora(LocalDateTime.now().minusDays(1))
                .local("Local Test")
                .capacidade(100)
                .categoriaId(1L)
                .gratuito(false)
                .preco(BigDecimal.TEN)
                .build();

        // Act & Assert
        assertThrows(BusinessException.class, () -> eventoService.criarEvento(dto));
        verify(eventoRepository, never()).save(any(Evento.class));
    }

    @Test
    void deveGerarErroAoCriarEventoPagoSemPreco() {
        // Arrange
        EventoDTO dto = EventoDTO.builder()
                .nome("Evento Test")
                .descricao("Descrição Test")
                .dataHora(LocalDateTime.now().plusDays(1))
                .local("Local Test")
                .capacidade(100)
                .categoriaId(1L)
                .gratuito(false)
                .preco(BigDecimal.ZERO)
                .build();

        // Act & Assert
        assertThrows(BusinessException.class, () -> eventoService.criarEvento(dto));
        verify(eventoRepository, never()).save(any(Evento.class));
    }

    @Test
    void deveGerarErroQuandoCategoriaInexistente() {
        // Arrange
        EventoDTO dto = EventoDTO.builder()
                .nome("Evento Test")
                .descricao("Descrição Test")
                .dataHora(LocalDateTime.now().plusDays(1))
                .local("Local Test")
                .capacidade(100)
                .categoriaId(999L)
                .gratuito(true)
                .build();

        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> eventoService.criarEvento(dto));
        verify(eventoRepository, never()).save(any(Evento.class));
    }
}