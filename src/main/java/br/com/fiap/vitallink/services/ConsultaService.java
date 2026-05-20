package br.com.fiap.vitallink.services;

import br.com.fiap.vitallink.dto.ConsultaRequestDTO;
import br.com.fiap.vitallink.dto.ConsultaResponseDTO;
import br.com.fiap.vitallink.entities.Consulta;
import br.com.fiap.vitallink.enums.StatusConsulta;
import br.com.fiap.vitallink.exceptions.ResourceNotFoundException;
import br.com.fiap.vitallink.repositories.ConsultaRepository;
import br.com.fiap.vitallink.repositories.MedicoRepository;
import br.com.fiap.vitallink.repositories.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public Page<ConsultaResponseDTO> listar(StatusConsulta status,
                                            Long medicoId,
                                            Long pacienteId,
                                            Pageable pageable) {
        if (status != null)     return consultaRepository.findByStatus(status, pageable).map(this::toResponse);
        if (medicoId != null)   return consultaRepository.findByMedicoId(medicoId, pageable).map(this::toResponse);
        if (pacienteId != null) return consultaRepository.findByPacienteId(pacienteId, pageable).map(this::toResponse);
        return consultaRepository.findAll(pageable).map(this::toResponse);
    }

    public Page<ConsultaResponseDTO> listarPorPeriodo(LocalDateTime inicio,
                                                      LocalDateTime fim,
                                                      Pageable pageable) {
        return consultaRepository.findByPeriodo(inicio, fim, pageable).map(this::toResponse);
    }

    public ConsultaResponseDTO buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public ConsultaResponseDTO criar(ConsultaRequestDTO dto) {
        var paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente não encontrado com id: " + dto.pacienteId()));
        var medico = medicoRepository.findById(dto.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médico não encontrado com id: " + dto.medicoId()));

        var consulta = Consulta.builder()
                .paciente(paciente)
                .medico(medico)
                .dataHora(dto.dataHora())
                .status(dto.status())
                .observacoes(dto.observacoes())
                .build();
        return toResponse(consultaRepository.save(consulta));
    }

    @Transactional
    public ConsultaResponseDTO atualizar(Long id, ConsultaRequestDTO dto) {
        var consulta = findOrThrow(id);

        var paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente não encontrado com id: " + dto.pacienteId()));
        var medico = medicoRepository.findById(dto.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médico não encontrado com id: " + dto.medicoId()));

        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(dto.dataHora());
        consulta.setStatus(dto.status());
        consulta.setObservacoes(dto.observacoes());
        return toResponse(consultaRepository.save(consulta));
    }

    @Transactional
    public void deletar(Long id) {
        var consulta = findOrThrow(id);
        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }

    private Consulta findOrThrow(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consulta não encontrada com id: " + id));
    }

    private ConsultaResponseDTO toResponse(Consulta c) {
        String nomeEsp = c.getMedico().getEspecialidade() != null
                ? c.getMedico().getEspecialidade().getNome() : null;
        return new ConsultaResponseDTO(
                c.getId(),
                c.getPaciente().getId(),
                c.getPaciente().getNome(),
                c.getMedico().getId(),
                c.getMedico().getNome(),
                c.getMedico().getCrm(),
                nomeEsp,
                c.getDataHora(),
                c.getStatus(),
                c.getObservacoes()
        );
    }
}