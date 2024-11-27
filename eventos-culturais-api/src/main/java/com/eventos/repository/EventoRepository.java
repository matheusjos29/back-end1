package com.eventos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventos.domain.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByNomeContainingIgnoreCase(String nome);
    List<Evento> findByDataHoraGreaterThanEqual(LocalDateTime data);
    List<Evento> findByCategoriaId(Long categoriaId);
}