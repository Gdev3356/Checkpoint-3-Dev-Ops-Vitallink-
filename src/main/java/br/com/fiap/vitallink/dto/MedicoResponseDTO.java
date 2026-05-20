package br.com.fiap.vitallink.dto;

public record MedicoResponseDTO(
        Long id,
        String nome,
        String crm,
        String email,
        String especialidade,
        String ativo
) {}