package com.eventos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {
    
    @NotNull(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotNull(message = "Descrição é obrigatória")
    @Size(min = 10, max = 500, message = "Descrição deve ter entre 10 e 500 caracteres")
    private String descricao;
    
    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "A data do evento deve ser futura")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;
    
    @NotNull(message = "Local é obrigatório")
    @Size(min = 3, max = 200, message = "Local deve ter entre 3 e 200 caracteres")
    private String local;
    
    @NotNull(message = "Capacidade é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser maior que zero")
    private Integer capacidade;
    
    @NotNull(message = "Categoria é obrigatória")
    private Long categoriaId;
    
    @NotNull(message = "Informe se o evento é gratuito")
    private boolean gratuito;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    private BigDecimal preco;
}
