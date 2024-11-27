package com.eventos.service;

import com.eventos.domain.Categoria;
import com.eventos.domain.Evento;
import com.eventos.dto.EventoDTO;
import com.eventos.exception.BusinessException;
import com.eventos.exception.ResourceNotFoundException;
import com.eventos.repository.CategoriaRepository;
import com.eventos.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventoService {

    private final EventoRepository eventoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional
    public Evento criarEvento(EventoDTO dto) {
        log.info("Criando novo evento: {}", dto.getNome());
        validarEvento(dto);
        
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Evento evento = Evento.builder()
            .nome(dto.getNome())
            .descricao(dto.getDescricao())
            .dataHora(dto.getDataHora())
            .local(dto.getLocal())
            .capacidade(dto.getCapacidade())
            .categoria(categoria)
            .gratuito(dto.isGratuito())
            .preco(dto.getPreco())
            .build();

        return eventoRepository.save(evento);
    }

    public List<Evento> buscarEventosPorNome(String nome) {
        log.info("Buscando eventos por nome: {}", nome);
        if (nome == null || nome.isEmpty()) {
            return eventoRepository.findAll();
        }
        return eventoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Evento> buscarEventosPorCategoria(Long categoriaId) {
        log.info("Buscando eventos por categoria: {}", categoriaId);
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
        return eventoRepository.findByCategoriaId(categoriaId);
    }

    @Transactional
    public Evento atualizarEvento(Long id, EventoDTO dto) {
        log.info("Atualizando evento: {}", id);
        validarEvento(dto);

        Evento evento = eventoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        evento.setNome(dto.getNome());
        evento.setDescricao(dto.getDescricao());
        evento.setDataHora(dto.getDataHora());
        evento.setLocal(dto.getLocal());
        evento.setCapacidade(dto.getCapacidade());
        evento.setCategoria(categoria);
        evento.setGratuito(dto.isGratuito());
        evento.setPreco(dto.getPreco());

        return eventoRepository.save(evento);
    }

    @Transactional
    public void deletarEvento(Long id) {
        log.info("Deletando evento: {}", id);
        if (!eventoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evento não encontrado");
        }
        eventoRepository.deleteById(id);
    }

    private void validarEvento(EventoDTO dto) {
        if (!dto.isGratuito() && (dto.getPreco() == null || dto.getPreco().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new BusinessException("Evento não gratuito deve ter preço maior que zero");
        }

        if (dto.getDataHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data do evento deve ser futura");
        }

        if (dto.getCapacidade() <= 0) {
            throw new BusinessException("Capacidade deve ser maior que zero");
        }
    }
}