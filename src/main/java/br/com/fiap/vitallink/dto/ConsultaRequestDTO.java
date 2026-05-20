package br.com.fiap.vitallink.dto;

import br.com.fiap.vitallink.enums.StatusConsulta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(

        @NotNull(message = "Paciente é obrigatório")
        Long pacienteId,

        @NotNull(message = "Médico é obrigatório")
        Long medicoId,

        @NotNull(message = "Data e hora são obrigatórias")
        LocalDateTime dataHora,

        @NotNull(message = "Status é obrigatório")
        StatusConsulta status,

        @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
        String observacoes
) {}