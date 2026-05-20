package br.com.fiap.vitallink.services;

import br.com.fiap.vitallink.dto.PacienteRequestDTO;
import br.com.fiap.vitallink.dto.PacienteResponseDTO;
import br.com.fiap.vitallink.entities.Paciente;
import br.com.fiap.vitallink.exceptions.BusinessException;
import br.com.fiap.vitallink.exceptions.ResourceNotFoundException;
import br.com.fiap.vitallink.repositories.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;

    public Page<PacienteResponseDTO> listar(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome, pageable).map(this::toResponse);
        }
        return repository.findAll(pageable).map(this::toResponse);
    }

    public PacienteResponseDTO buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public PacienteResponseDTO criar(PacienteRequestDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new BusinessException("CPF '" + dto.cpf() + "' já está cadastrado.");
        }
        if (repository.existsByEmail(dto.email())) {
            throw new BusinessException("Email '" + dto.email() + "' já está em uso.");
        }
        var paciente = Paciente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .email(dto.email())
                .telefone(dto.telefone())
                .dataNascimento(dto.dataNascimento())
                .build();
        return toResponse(repository.save(paciente));
    }

    @Transactional
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {
        var paciente = findOrThrow(id);
        paciente.setNome(dto.nome());
        paciente.setEmail(dto.email());
        paciente.setTelefone(dto.telefone());
        paciente.setDataNascimento(dto.dataNascimento());
        return toResponse(repository.save(paciente));
    }

    @Transactional
    public void deletar(Long id) {
        findOrThrow(id);
        repository.deleteById(id);
    }

    private Paciente findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente não encontrado com id: " + id));
    }

    private PacienteResponseDTO toResponse(Paciente p) {
        return new PacienteResponseDTO(
                p.getId(), p.getNome(), p.getCpf(), p.getEmail(),
                p.getTelefone(), p.getDataNascimento(), p.getDataCadastro()
        );
    }
}