package com.eventos.dto.mapper;

import com.eventos.domain.Evento;
import com.eventos.dto.CategoriaDTO;
import com.eventos.dto.EventoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {
    
    public EventoResponseDTO toDTO(Evento evento) {
        if (evento == null) {
            return null;
        }

        return EventoResponseDTO.builder()
            .id(evento.getId())
            .nome(evento.getNome())
            .descricao(evento.getDescricao())
            .dataHora(evento.getDataHora())
            .local(evento.getLocal())
            .capacidade(evento.getCapacidade())
            .categoria(CategoriaDTO.builder()
                .nome(evento.getCategoria().getNome())
                .descricao(evento.getCategoria().getDescricao())
                .build())
            .gratuito(evento.isGratuito())
            .preco(evento.getPreco())
            .build();
    }
}