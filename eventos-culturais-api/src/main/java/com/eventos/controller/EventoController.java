package com.eventos.controller;

import com.eventos.domain.Evento;
import com.eventos.dto.EventoDTO;
import com.eventos.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "API de gerenciamento de eventos")
@SecurityRequirement(name = "bearerAuth")
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo evento", description = "Cria um novo evento no sistema")
    public ResponseEntity<Evento> criarEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        return ResponseEntity.ok(eventoService.criarEvento(eventoDTO));
    }

    @GetMapping
    @Operation(summary = "Buscar eventos", description = "Busca eventos por nome (opcional)")
    public ResponseEntity<List<Evento>> buscarEventos(@RequestParam(required = false) String nome) {
        return ResponseEntity.ok(eventoService.buscarEventosPorNome(nome));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Buscar eventos por categoria", description = "Busca eventos de uma categoria específica")
    public ResponseEntity<List<Evento>> buscarEventosPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(eventoService.buscarEventosPorCategoria(categoriaId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar evento", description = "Atualiza um evento existente")
    public ResponseEntity<Evento> atualizarEvento(@PathVariable Long id, @Valid @RequestBody EventoDTO eventoDTO) {
        return ResponseEntity.ok(eventoService.atualizarEvento(id, eventoDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar evento", description = "Remove um evento do sistema")
    public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
        eventoService.deletarEvento(id);
        return ResponseEntity.noContent().build();
    }
}
