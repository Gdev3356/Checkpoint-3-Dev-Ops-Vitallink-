package br.com.fiap.vitallink.dto;


import br.com.fiap.vitallink.enums.StatusConsulta;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        Long pacienteId,
        String nomePaciente,
        Long medicoId,
        String nomeMedico,
        String crm,
        String especialidade,
        LocalDateTime dataHora,
        StatusConsulta status,
        String observacoes
) {}