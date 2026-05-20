package br.com.fiap.vitallink.services;

import br.com.fiap.vitallink.dto.MedicoRequestDTO;
import br.com.fiap.vitallink.dto.MedicoResponseDTO;
import br.com.fiap.vitallink.entities.Medico;
import br.com.fiap.vitallink.exceptions.BusinessException;
import br.com.fiap.vitallink.exceptions.ResourceNotFoundException;
import br.com.fiap.vitallink.repositories.EspecialidadeRepository;
import br.com.fiap.vitallink.repositories.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadeRepository especialidadeRepository;

    public Page<MedicoResponseDTO> listar(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return medicoRepository
                    .findByAtivoAndNomeContainingIgnoreCase("S", nome, pageable)
                    .map(this::toResponse);
        }
        return medicoRepository.findByAtivo("S", pageable).map(this::toResponse);
    }

    public Page<MedicoResponseDTO> listarPorEspecialidade(Long especialidadeId, Pageable pageable) {
        return medicoRepository
                .findByAtivoAndEspecialidadeId("S", especialidadeId, pageable)
                .map(this::toResponse);
    }

    public MedicoResponseDTO buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public MedicoResponseDTO criar(MedicoRequestDTO dto) {
        if (medicoRepository.existsByCrm(dto.crm())) {
            throw new BusinessException("CRM '" + dto.crm() + "' já está cadastrado.");
        }
        if (medicoRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Email '" + dto.email() + "' já está em uso.");
        }
        var especialidade = especialidadeRepository.findById(dto.especialidadeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Especialidade não encontrada com id: " + dto.especialidadeId()));

        var medico = Medico.builder()
                .nome(dto.nome())
                .crm(dto.crm())
                .email(dto.email())
                .especialidade(especialidade)
                .ativo("S")
                .build();
        return toResponse(medicoRepository.save(medico));
    }

    @Transactional
    public MedicoResponseDTO atualizar(Long id, MedicoRequestDTO dto) {
        var medico = findOrThrow(id);
        var especialidade = especialidadeRepository.findById(dto.especialidadeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Especialidade não encontrada com id: " + dto.especialidadeId()));

        medico.setNome(dto.nome());
        medico.setEmail(dto.email());
        medico.setEspecialidade(especialidade);
        return toResponse(medicoRepository.save(medico));
    }

    @Transactional
    public void deletar(Long id) {
        var medico = findOrThrow(id);
        medico.setAtivo("N");
        medicoRepository.save(medico);
    }

    private Medico findOrThrow(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Médico não encontrado com id: " + id));
    }

    private MedicoResponseDTO toResponse(Medico m) {
        String nomeEsp = m.getEspecialidade() != null ? m.getEspecialidade().getNome() : null;
        return new MedicoResponseDTO(m.getId(), m.getNome(), m.getCrm(),
                m.getEmail(), nomeEsp, m.getAtivo());
    }
}