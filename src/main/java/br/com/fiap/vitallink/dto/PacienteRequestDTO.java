package br.com.fiap.vitallink.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PacienteRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "CPF inválido. Use o formato 000.000.000-00")
        String cpf,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
        String email,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Past(message = "Data de nascimento deve ser uma data passada")
        LocalDate dataNascimento
) {}