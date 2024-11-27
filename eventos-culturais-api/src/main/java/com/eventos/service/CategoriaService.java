package com.eventos.service;

import com.eventos.domain.Categoria;
import com.eventos.dto.CategoriaDTO;
import com.eventos.exception.BusinessException;
import com.eventos.exception.ResourceNotFoundException;
import com.eventos.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria criarCategoria(CategoriaDTO dto) {
        log.info("Criando nova categoria: {}", dto.getNome());
        
        if (categoriaRepository.existsByNome(dto.getNome())) {
            throw new BusinessException("Categoria já existe com este nome");
        }

        Categoria categoria = Categoria.builder()
            .nome(dto.getNome())
            .descricao(dto.getDescricao())
            .build();

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategorias() {
        log.info("Listando todas as categorias");
        return categoriaRepository.findAll();
    }

    public Categoria buscarCategoriaPorId(Long id) {
        log.info("Buscando categoria por ID: {}", id);
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    @Transactional
    public Categoria atualizarCategoria(Long id, CategoriaDTO dto) {
        log.info("Atualizando categoria: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Categoria categoriaComMesmoNome = categoriaRepository.findByNome(dto.getNome())
            .orElse(null);
        
        if (categoriaComMesmoNome != null && !categoriaComMesmoNome.getId().equals(id)) {
            throw new BusinessException("Já existe outra categoria com este nome");
        }

        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void deletarCategoria(Long id) {
        log.info("Deletando categoria: {}", id);
        
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
        
        categoriaRepository.deleteById(id);
    }
}